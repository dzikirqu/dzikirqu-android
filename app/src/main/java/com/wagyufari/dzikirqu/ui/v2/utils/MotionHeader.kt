//package com.wagyufari.dzikirqu.ui.v2.utils
//
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.layout.layoutId
//import androidx.constraintlayout.compose.ConstraintSet
//import androidx.constraintlayout.compose.Dimension
//import androidx.constraintlayout.compose.MotionLayout
//
//
//@Composable
//fun MotionComposeHeader(
//    progress: Float,
//    header: @Composable () -> Unit,
//    content: @Composable () -> Unit
//) {
//    MotionLayout(
//        progress = progress,
//        modifier = Modifier
//            .fillMaxWidth(),
//        start = startConstraintSet(),
//        end = endConstraintSet()
//    ) {
//        Column(Modifier.layoutId("header")) {
//            header()
//        }
//        Box(
//            Modifier.layoutId("content")
//        ) {
//            content()
//        }
//    }
//}
//
//private fun startConstraintSet() = ConstraintSet {
//    val header = createRefFor("header")
//    val content = createRefFor("content")
//
//    constrain(header) {
//        width = Dimension.fillToConstraints
//        top.linkTo(parent.top)
//        start.linkTo(parent.start)
//        end.linkTo(parent.end)
//    }
//
//    constrain(content) {
//        width = Dimension.fillToConstraints
//        top.linkTo(header.bottom)
//        start.linkTo(parent.start)
//        end.linkTo(parent.end)
//    }
//}
//
//private fun endConstraintSet() = ConstraintSet {
//    val header = createRefFor("header")
//    val content = createRefFor("content")
//
//    constrain(header) {
//        width = Dimension.fillToConstraints
//        top.linkTo(parent.top)
//        start.linkTo(parent.start)
//        end.linkTo(parent.end)
//    }
//
//    constrain(content) {
//        width = Dimension.fillToConstraints
//        top.linkTo(parent.bottom)
//        start.linkTo(parent.start)
//        end.linkTo(parent.end)
//        bottom.linkTo(parent.bottom)
//    }
//}
