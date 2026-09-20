                 modifier =
                    Modifier.padding(
                        24.dp
                    )

            ) {

                Box(

                    modifier =
                        Modifier
                            .size(55.dp)
                            .clip(
                                CircleShape
                            )
                            .background(
                                Color.White.copy(
                                    alpha = 0.15f
                                )
                            ),

                    contentAlignment =
                        Alignment.Center

                ) {

                    Icon(

                        imageVector =
                            Icons.Default.Star,

                        contentDescription =
                            "SHILOH",

                        tint =
                            Gold,

                        modifier =
                            Modifier.size(
                                30.dp
                            )
                    )
                }

                Spacer(
                    modifier =
                        Modifier.height(18.dp)
                )

                Text(

                    text =
                        "LEMEKEZANI MULUNGU M'NYIMBO",

                    fontSize =
                        21.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        Color.White
                )

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                Text(

                    text =
                        "SHILOH SDB CHURCH",

                    fontSize =
                        14.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        Color
