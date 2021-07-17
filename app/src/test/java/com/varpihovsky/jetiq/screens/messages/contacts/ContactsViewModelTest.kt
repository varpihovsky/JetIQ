package com.varpihovsky.jetiq.screens.messages.contacts

import com.varpihovsky.core.util.Selectable
import com.varpihovsky.core_repo.repo.ListRepo
import com.varpihovsky.jetiq.screens.messages.create.NewMessageViewModel
import com.varpihovsky.jetiq.testCore.ViewModelDataTransferTest
import com.varpihovsky.repo_data.ContactDTO
import com.varpihovsky.ui_data.mappers.toUIDTO
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.Assert.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class ContactsViewModelTest : ViewModelDataTransferTest() {
    private lateinit var contactsViewModel: ContactsViewModel
    private val listModel: ListRepo = mockk(relaxed = true)

    @ExperimentalCoroutinesApi
    private fun initViewModel() {
        contactsViewModel = ContactsViewModel(
            viewModelDispatchers,
            appbarManager,
            navigationController,
            listModel,
            viewModelDataTransferManager
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test database contacts are collected`() = runBlockingTest {
        defaultInit()

        assertEquals(
            TEST_CONTACTS.map { Selectable(it.toUIDTO(), false) },
            contactsViewModel.data.contacts.takeLast(listOf(), this)
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test database contacts are sorted`() = runBlockingTest {
        setupListModel(listOf(TEST_CONTACTS.last(), TEST_CONTACTS.first()))
        initViewModel()

        assertEquals(
            TEST_CONTACTS.map { Selectable(it.toUIDTO(), false) },
            contactsViewModel.data.contacts.takeLast(listOf(), this)
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test transferred contacts are selected`() = runBlockingTest {
        defaultInit()
        transferContacts(TEST_CONTACTS.first())

        assertEquals(
            listOf(
                Selectable(TEST_CONTACTS.first().toUIDTO(), true),
                Selectable(TEST_CONTACTS[1].toUIDTO(), false)
            ),
            contactsViewModel.data.contacts.takeLast(listOf(), this)
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test click is enabled and long click is disabled when external contacts are transfered`() =
        runBlockingTest {
            defaultInit()
            transferContacts(TEST_CONTACTS.first())
            assertClicks(true)
        }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test isExternalChoosing is true when external contacts are transferred`() =
        runBlockingTest {
            defaultInit()
            transferContacts(TEST_CONTACTS.first())
            assertTrue(contactsViewModel.data.isExternalChoosing.value)
        }


    @ExperimentalCoroutinesApi
    @Test
    fun `Test click is enabled and long click is disable when element is chosen`() =
        runBlockingTest {
            defaultInit()
            longClick(TEST_CONTACTS.first())
            assertClicks(true)
        }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test long click is enabled by default`() = runBlockingTest {
        defaultInit()
        assertClicks(false)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test selected item is removed`() = runBlockingTest {
        defaultInit()
        longClick(TEST_CONTACTS.first())
        contactsViewModel.onRemoveClick()
        verify { listModel.removeContact(TEST_CONTACTS.first()) }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test all selected contacts are removed`() = runBlockingTest {
        defaultInit()
        longClick(TEST_CONTACTS.first())
        click(TEST_CONTACTS.last())
        contactsViewModel.onRemoveClick()
        TEST_CONTACTS.forEach { verify { listModel.removeContact(it) } }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test adding is enabled when add button clicked`() = runBlockingTest {
        defaultInit()
        contactsViewModel.onAddClick()
        assertTrue(contactsViewModel.data.isAdding.value)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test search field is changing`() = runBlockingTest {
        defaultInit()
        contactsViewModel.onSearchFieldValueChange(TEST_QUERY)
        assertEquals(TEST_QUERY, contactsViewModel.data.searchFieldValue.value)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test contacts are filtered on field changes`() = runBlockingTest {
        defaultInit()
        contactsViewModel.onSearchFieldValueChange(TEST_QUERY)

        assertEquals(
            listOf(Selectable(TEST_CONTACTS.first().toUIDTO(), false)),
            contactsViewModel.data.contacts.takeLast(listOf(), this)
        )
    }

    @ExperimentalCoroutinesApi
    private fun defaultInit() {
        setupListModel(TEST_CONTACTS)
        initViewModel()
    }

    private fun setupListModel(data: List<ContactDTO>) {
        every { listModel.getContacts() } returns flow { emit(data) }
    }

    private fun transferContacts(vararg contacts: ContactDTO) {
        dataTransferStateFlow.value = ContactsViewModelData(
            listOf(*contacts).map { it.toUIDTO() },
            NewMessageViewModel::class
        )
    }

    private fun longClick(contact: ContactDTO) {
        contactsViewModel.onContactLongClick(Selectable(contact.toUIDTO(), false))
    }

    private fun click(vararg contacts: ContactDTO) {
        contacts.map { Selectable(it.toUIDTO(), false) }
            .forEach { contactsViewModel.onContactClick(it) }
    }

    private fun assertClicks(isClickEnabledExpected: Boolean) {
        assertEquals(isClickEnabledExpected, contactsViewModel.data.isClickEnabled.value)
        assertNotSame(isClickEnabledExpected, contactsViewModel.data.isLongClickEnabled.value)
    }

    companion object {
        val TEST_CONTACTS = listOf(
            ContactDTO(0, "Sample Contact 1", ContactDTO.TYPE_TEACHER),
            ContactDTO(1, "Sample Contact 2", ContactDTO.TYPE_STUDENT)
        )

        private const val TEST_QUERY = "1"
    }
}