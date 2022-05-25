package com.example.trainningproject

const val KEY_MESS = "key_mess"

sealed class Screen(val route: String) {
    object First : Screen("first_screen")
    object Second : Screen("second_screen?mess={$KEY_MESS}") {
        fun passId(mess: String) : String {
            return "second_screen?mess=$mess"
        }
    }
}
