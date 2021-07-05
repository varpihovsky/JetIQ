package com.varpihovsky.jetiq

class SharedViewModelTest {
//    private lateinit var sharedViewModel: SharedViewModel
//    private val profileModel: ProfileModel = mockk()
//
//    @Before
//    fun setup() {
//        sharedViewModel = SharedViewModel(profileModel)
//    }
//
//    @Test
//    fun `Test getting main start destination when there is no user confidential in db`() {
//        every { profileModel.getConfidential() } returns flow { }
//        assertEquals(
//            NavigationDirections.authentication.destination,
//            sharedViewModel.getStartDestination()
//        )
//    }
//
//    @Test
//    fun `Test get main start destination where there is user confidential in db`() {
//        every { profileModel.getConfidential() } returns flow {
//            emit(
//                Confidential(
//                    "someuser",
//                    "somepassword"
//                )
//            )
//        }
//        assertEquals(
//            NavigationDirections.profile.destination,
//            sharedViewModel.getStartDestination()
//        )
//    }
//
//    @After
//    fun teardown() {
//        unmockkAll()
//    }
}