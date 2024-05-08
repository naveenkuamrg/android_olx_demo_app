package com.application.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.application.R
import com.application.helper.Utility
import com.application.model.Notification
import com.application.model.NotificationType


@Composable
fun NotificationItemView(notification: Notification, onClickListener: (Long) -> Unit) {
    val backGroundColor = if (notification.isRead) {
        colorResource(R.color.md_theme_background)
    } else {
        colorResource(R.color.md_theme_primaryContainer)
    }

    Column(
        Modifier
            .background(backGroundColor)
            .fillMaxWidth()
            .focusable(true)
            .clickable {
                onClickListener(notification.id)
            }
    ) {
        Text(
            text = notification.content,
            Modifier
                .fillMaxWidth()
                .padding(10.dp, 10.dp, 0.dp, 0.dp),
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight(600),
            color = colorResource(id = R.color.md_theme_inverseSurface),
            maxLines = 3
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = Utility.setCreatedTime(notification.timestamp),
            Modifier
                .fillMaxWidth()
                .padding(10.dp, 0.dp, 0.dp, 10.dp),
            fontWeight = FontWeight(400),
            color = colorResource(id = R.color.md_theme_inverseSurface),
            fontSize = 14.sp
        )
    }

}

@Composable
fun NotificationRecyclerView(){

}

@Preview()
@Composable
fun CheckPreview() {
    MaterialTheme {
        NotificationItemView(
            Notification(
                1,
                231L,
                true,
                "Content",
                NotificationType.PROFILE
            )
        ) {
        }
    }
}