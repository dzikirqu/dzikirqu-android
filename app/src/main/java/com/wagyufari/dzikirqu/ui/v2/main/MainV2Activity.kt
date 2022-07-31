//package com.wagyufari.dzikirqu.ui.v2.main
//
//import android.content.Context
//import android.content.Intent
//import android.os.Bundle
//import android.view.View
//import androidx.activity.compose.setContent
//import androidx.activity.viewModels
//import androidx.appcompat.app.AppCompatActivity
//import androidx.compose.material.*
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.colorResource
//import androidx.compose.ui.res.painterResource
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.navigation.NavController
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.currentBackStackEntryAsState
//import androidx.navigation.compose.rememberNavController
//import com.wagyufari.dzikirqu.R
//import com.wagyufari.dzikirqu.ui.v2.main.home.MainHomeScreen
//import com.wagyufari.dzikirqu.ui.v2.main.quran.MainQuranScreen
//import com.wagyufari.dzikirqu.ui.v2.theme.lato
//import dagger.hilt.android.AndroidEntryPoint
//
//@AndroidEntryPoint
//class MainV2Activity : AppCompatActivity() {
//
//    val viewModel: MainV2ViewModel by viewModels()
//
//    companion object {
//        fun start(mContext: Context) {
//            mContext.startActivity(Intent(mContext, MainV2Activity::class.java))
//        }
//
//        fun newIntent(mContext: Context): Intent {
//            return Intent(mContext, MainV2Activity::class.java)
//        }
//
//        fun start(view: View) {
//            val mContext = view.context
//            mContext.startActivity(Intent(mContext, MainV2Activity::class.java))
//        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            MainScreenView()
//        }
//    }
//}
//
//@Composable
//fun MainScreenView() {
//    val navController = rememberNavController()
//    Scaffold(
//        bottomBar = { BottomNavigation(navController = navController) }
//    ) {
//        NavigationGraph(navController = navController)
//    }
//}
//
//@Composable
//fun BottomNavigation(navController: NavController) {
//    val items = listOf(
//        BottomNavItem.Home,
//        BottomNavItem.Quran,
//        BottomNavItem.Bookmarks,
//    )
//    BottomNavigation(
//        backgroundColor = Color.White,
//        contentColor = Color.Black
//    ) {
//        val navBackStackEntry by navController.currentBackStackEntryAsState()
//        val currentRoute = navBackStackEntry?.destination?.route
//        items.forEach { item ->
//            BottomNavigationItem(
//                icon = { Icon(painterResource(id = item.icon), contentDescription = item.title) },
//                label = {
//                    Text(
//                        text = item.title,
//                        fontFamily = lato
//                    )
//                },
//                selectedContentColor = colorResource(id = R.color.colorPrimary),
//                unselectedContentColor = Color.Black.copy(0.4f),
//                alwaysShowLabel = true,
//                selected = currentRoute == item.screen_route,
//                onClick = {
//                    navController.navigate(item.screen_route) {
//                        navController.graph.startDestinationRoute?.let { screen_route ->
//                            popUpTo(screen_route) {
//                                saveState = true
//                            }
//                        }
//                        launchSingleTop = true
//                        restoreState = true
//                    }
//                }
//            )
//        }
//    }
//}
//
//@Composable
//fun NavigationGraph(navController: NavHostController) {
//    NavHost(navController, startDestination = BottomNavItem.Home.screen_route) {
//        composable(BottomNavItem.Home.screen_route) {
//            MainHomeScreen(hiltViewModel(), navController)
//        }
//        composable(BottomNavItem.Quran.screen_route) {
//            MainQuranScreen(hiltViewModel())
//        }
//        composable(BottomNavItem.Bookmarks.screen_route) {
//            MainHomeScreen(hiltViewModel(), navController)
//        }
//    }
//}
//
//sealed class BottomNavItem(var title: String, var icon: Int, var screen_route: String) {
//    object Home : BottomNavItem("Home", R.drawable.ic_home, "home")
//    object Quran : BottomNavItem("Al-Qur'an", R.drawable.ic_menu_book, "quran")
//    object Bookmarks : BottomNavItem("Bookmarks", R.drawable.ic_bookmarks, "bookmarks")
//}
