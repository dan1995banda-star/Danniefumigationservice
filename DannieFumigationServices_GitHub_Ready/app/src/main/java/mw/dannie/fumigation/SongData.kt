package mw.dannie.fumigation

object SongData {

    val songs = listOf(

        Song(
            number = 1,
            title = "AITANA, AITANA DZIKO LONSE",
            englishTitle = "He Calls the Whole World",
            verses = listOf(
                """
Aitana, aitana dziko lonse,
Imvani, mukhaliranji chete? Aitana,
Aitana, aitana dziko lonse.
                """.trimIndent(),

                """
Mfumu iyi, Mfumu iyi yakumwamba,
Ndi Yesu, ndi Mpulumutsiyo wa anthu onse,
Anthu onse, anthu onse, tinke kwawo.
                """.trimIndent(),

                """
Aitana, aitana inu mai,
Ndi Yesu, amene anakhetsa mwazi wake;
Mwazi wake unagwera inu mai.
                """.trimIndent(),

                """
Aitana, aitana inu 'tate,
Ndi Yesu amene anakhetsa mwazi wake:
Mwazi wake unagwera inu 'tate.
                """.trimIndent(),

                """
Yesu ati, Yesu ati: "Muzisiye
Zoipa zimene muzichita, muzisiye,
Muzisiye, muzisiye inu nonse."
                """.trimIndent(),

                """
Titi bwanji? Titi bwanji ife anthu?
Ndi Yesu anatifera ife akuchimwa;
Akuchimwa, akuchimwa tinke kwawo.
                """.trimIndent()
            )
        ),

        Song(
            number = 2,
            title = "AKAN'TSOGOZA NDINKAKO",
            englishTitle = "All the Way",
            verses = listOf(
                """
Akan'tsogoza ndinkako, Ndikhulupira Mbuyeyo;
Ndikumbukira kutitu, Anandifera pa mtanda.
Yesu atsogolerane, Ulendo wanga wonsewo.
Bwenzi loona ndiyeyo, Ndikumbukira mtandawo.
                """.trimIndent(),

                """
Ndikonda mau akewo, Anditsogoza Mbuyeyo;
Kufuna kwake ndichite, Anandifera pa mtanda.
                """.trimIndent(),

                """
Ndzapita osaopai, Akhale nane Yesuyo;
Pa tsiku lina n'dzaona, Yesu tsamwali wa mtanda.
                """.trimIndent()
            )
        ),

        Song(
            number = 3,
            title = "AKHRISTU LIMBIKANI",
            englishTitle = "Stand Up for Jesus",
            verses = listOf(
                """
Akhristu limbikani, imani mtima nji!
Mbendela nyamulani, msanthenthemerebi,
Wotsogolera wathu ndi Mwana wa Mulungu;
Adzagonjetsa onse, adani athuwo.
                """.trimIndent(),

                """
Tigwire nkhondo yomwe ya Mbuye wathuyo;
Tichotse zakuipa zokhala mtimamo.
Sitikwanira tokha kuleka zathu zomwe;
Koma Mbuyathu Yesu atithangatatu.
                """.trimIndent(),

                """
Tsopano timenyana ndi zakuipazo;
Pamene nkhondo yatha tidzanka kwathuko;
Yesu adzalandira ankhondo ake onse,
Adzakhatitsa naye kumwamba komweko.
                """.trimIndent(),

                """
Limbikani anzathu! msanga nkhondo itha;
Lero kuli chinkhondo, mawa tigonjetsa.
Kwa iye wolakika, adzakhala moyo;
Pamodzi ndi Mfumuyo dzakhala kosatha.
                """.trimIndent()
            )
        )
    )
}

The three songs and their English titles are taken from the uploaded hymn book.

Do this now

1. Create "SongData.kt"
2. Paste the code exactly.
3. Commit it.
4. Wait for GitHub Actions.
5. Tell me whether it says Success.

After that, we'll connect "SongData.kt" to "MainActivity.kt" so tapping Song 1, Song 2, or Song 3 opens the correct individual page.
