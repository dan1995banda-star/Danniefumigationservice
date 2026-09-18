                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "CONTACT US",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "Reliable • Professional • Affordable",
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}
