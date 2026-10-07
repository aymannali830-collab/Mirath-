package com.example.data

import com.example.model.*

/**
 * Mirath Normalized Repository
 * Single Source of Truth for Games, Countries, Regions, Glossary, and Guidelines.
 * Strict documentation policy: No fabricated dates or origins.
 */
object MirathRepository {

    val regions: List<Region> = listOf(
        Region(
            id = "middle_east",
            nameAr = "الشرق الأوسط وشمال أفريقيا",
            nameEn = "Middle East & North Africa",
            summaryAr = "مهد أقدم ألعاب الألواح والسباق الإستراتيجي، من بلاد الرافدين ووادي النيل إلى شبه الجزيرة العربية."
        ),
        Region(
            id = "sub_saharan_africa",
            nameAr = "أفريقيا جنوب الصحراء",
            nameEn = "Sub-Saharan Africa",
            summaryAr = "موطن أعرق ألعاب الحصاد والتوزيع الإيقاعي كعائلة المانكالا والألواح الرملية المنحوتة."
        ),
        Region(
            id = "mediterranean",
            nameAr = "حوض البحر الأبيض المتوسط",
            nameEn = "Mediterranean",
            summaryAr = "ملتقى الثقافات الإيبيرية والرومانية والإغريقية، موطن ألعاب المحاصرة والتراصف التكتيكي."
        ),
        Region(
            id = "east_asia",
            nameAr = "شرق آسيا",
            nameEn = "East Asia",
            summaryAr = "ألعاب التوازن الفلسفي وحصار المساحات والتطويق الصامت الموثقة في المخطوطات الملكية."
        ),
        Region(
            id = "scandinavia",
            nameAr = "شمال أوروبا والدول الإسكندنافية",
            nameEn = "Northern Europe & Scandinavia",
            summaryAr = "ألعاب غير متناظرة تحاكي غارات الملوك ومحاولات الهروب التكتيكية فوق شبكات خشبية."
        )
    )

    val countries: List<Country> = listOf(
        Country(
            id = "iraq",
            nameAr = "العراق (بلاد الرافدين)",
            nameEn = "Iraq (Mesopotamia)",
            regionId = "middle_east",
            summaryAr = "عُثر في مقابر أور الملكية على أقدم ألواح اللعب المتكاملة في التاريخ الإنساني.",
            historicalGamingCultureAr = "ارتبطت الألعاب بالتكهن والرمزية الدينية وحظوظ القصور السومرية والأكادية."
        ),
        Country(
            id = "egypt",
            nameAr = "مصر القديمة",
            nameEn = "Ancient Egypt",
            regionId = "middle_east",
            summaryAr = "وثّقت الجداريات الفرعونية ألعاب السباق والعبور إلى الحياة الأخرى كالسنت ومحاصرة الكلاب والضباع.",
            historicalGamingCultureAr = "كانت ألعاب الألواح جزءاً من الطقوس الجنائزية والترويح البلاطي."
        ),
        Country(
            id = "kenya",
            nameAr = "كينيا وشرق أفريقيا",
            nameEn = "Kenya & East Africa",
            regionId = "sub_saharan_africa",
            summaryAr = "شهدت ألعاب التراصف وتوزيع البذور حيوية مجتمعية شعبية متوارثة شفهياً بين الأجيال.",
            historicalGamingCultureAr = "تُمارس في الساحات العامة والمدارس لتعزيز الحساب الذهني السريع والتركيز الإيقاعي."
        ),
        Country(
            id = "morocco_andalusia",
            nameAr = "المغرب والأندلس",
            nameEn = "Morocco & Al-Andalus",
            regionId = "mediterranean",
            summaryAr = "وثّق كتاب الألعاب للملك ألفونسو العاشر (1283م) انتقال ألعاب القرق والتربيع الإسلامية إلى أوروبا.",
            historicalGamingCultureAr = "تفاعل تكتيكي عميق بين الألعاب الاستراتيجية العربية والتقاليد المتوسطية."
        ),
        Country(
            id = "ghana",
            nameAr = "غانا وغرب أفريقيا",
            nameEn = "Ghana & West Africa",
            regionId = "sub_saharan_africa",
            summaryAr = "تُعتبر لعبة 'أواري' إحدى أعرق ألعاب الذكاء التي نُحتت ألواحها على الخشب الصلب كرموز مكانة وتواصل.",
            historicalGamingCultureAr = "تُقام لها بطولات رسمية في الثقافة الأكانية لتربية قادة المجتمع على إدارة الموارد بحكمة."
        ),
        Country(
            id = "scandinavia_region",
            nameAr = "إسكندنافيا (عصر الفايكنغ)",
            nameEn = "Scandinavia",
            regionId = "scandinavia",
            summaryAr = "طور الإسكندنافيون ألعاب التافيل (Tafl) الملكية غير المتناظرة للدفاع عن الملك وتطويقه.",
            historicalGamingCultureAr = "كانت وسيلة رئيسية لتدريب المحاربين على التخطيط الحربي في الليالي القطبية الطويلة."
        )
    )

    val glossary: List<GlossaryTerm> = listOf(
        GlossaryTerm(
            id = "mancala_sowing",
            slug = "mancala-sowing",
            termAr = "البَذْر (Sowing)",
            definitionAr = "آلية ميكانيكية تقليدية تقوم على التقاط جميع البذور أو الحصى من حفرة واحدة وتوزيعها حبة حبة بالتتابع في الحفر التالية باتجاه محدد.",
            originLanguageAr = "العربية (من نَقَلَ / مَنقَلة)",
            relatedGamesSlugs = listOf("oware", "hawalis", "kalah")
        ),
        GlossaryTerm(
            id = "asymmetry",
            slug = "game-asymmetry",
            termAr = "اللعب غير المتناظر (Asymmetric Play)",
            definitionAr = "نظام ألعاب يمتلك فيه كل طرف أهدافاً وقوى وقطعاً غير متطابقة، كأن يدافع لاعب بقطع قليلة ضد جيش مهاجم مضاعف.",
            originLanguageAr = "مصطلح دراسات ألعاب",
            relatedGamesSlugs = listOf("hnefatafl", "tablut")
        ),
        GlossaryTerm(
            id = "rosette",
            slug = "rosette-cell",
            termAr = "خانة الروزيت (الوردة)",
            definitionAr = "خانة خاصة منقوشة على لوح أور الملكي تمنح اللاعب دوراً إضافياً وتوفر لقطعته حماية مطلقة من الأسر من قطع الخصم.",
            originLanguageAr = "السومرية / الأكادية",
            relatedGamesSlugs = listOf("royal-game-of-ur")
        ),
        GlossaryTerm(
            id = "alquerque_leap",
            slug = "alquerque-leap",
            termAr = "القفز والأسر الإجباري (Leap Capture)",
            definitionAr = "قاعدة تحتم القفز فوق قطعة الخصم إلى خانة فارغة تليها مباشرة لإزالتها، وهي الجد التاريخي للعبة الدامة الحديثة.",
            originLanguageAr = "العربية (القِرق)",
            relatedGamesSlugs = listOf("alquerque", "dama")
        )
    )

    // Complete catalog of documented games
    val games: List<Game> = listOf(
        Game(
            id = "game_ur",
            slug = "royal-game-of-ur",
            titleAr = "لعبة أور الملكية",
            titleEn = "Royal Game of Ur",
            originalTitle = "Lu-malku (سومري مفترض)",
            aliases = listOf("لعبة العشرين مربعاً", "Game of Twenty Squares"),
            origin = GameOrigin(
                countryId = "iraq",
                regionId = "middle_east",
                culture = "الحضارة السومرية - بلاد الرافدين",
                status = OriginStatus.documented,
                claims = emptyList()
            ),
            historicalContext = HistoricalContext(
                summaryAr = "اكتشف عالم الآثار السير ليونارد وولي الألواح الملكية الفاخرة المطعمة بالعاج واللازورد في مقابر أور الملكية (حوالي 2600 قبل الميلاد). وفك الباحث إيرفينغ فينكل شفرة قواعدها من لوح مسماري بابلي يعود للعام 177 قبل الميلاد.",
                era = "2600 ق.م - القرن الثاني ق.م",
                notesAr = "تمثل أقدم قواعد ألعاب مدونة في التاريخ الإنساني محفوظة في المتحف البريطاني.",
                claims = listOf(
                    ClaimBlock(
                        claimId = "ur_c1",
                        textAr = "تطابقت نصوص لوح الكاتب إيتي-مردوخ-بالاطو المسماري مع قواعد حركة القطع ورمي النرد الهرمي رباعي الأوجه.",
                        sourceRefs = listOf("src_finkel_2007"),
                        status = ClaimStatus.supported
                    ),
                    ClaimBlock(
                        claimId = "ur_c2",
                        textAr = "تم العثور على نقش لنفس اللوح منحوتاً على جدار حارس ثور مجنح في قصر خورساباد الأشوري منقوشاً بخربشات جنود.",
                        sourceRefs = listOf("src_british_museum_ur"),
                        status = ClaimStatus.supported
                    )
                )
            ),
            players = PlayerSpecs(min = 2, max = 2, teamBased = false),
            estimatedPlayTime = PlayTimeSpecs(minMinutes = 15, maxMinutes = 25),
            timeToLearnMinutes = 5,
            rulesDifficulty = 2,
            masteryDifficulty = 4,
            primaryCategory = "سباق وتكتيك مسار",
            secondaryCategories = listOf("تاريخ قديم", "نرد واستراتيجية"),
            playStyle = listOf("رمي نرد هرمي", "إدارة مخاطر", "أسر القطع بالمطاردة"),
            tags = listOf("أور", "سومر", "متحف بريطاني", "نرد", "لوح خشبي"),
            whatMakesItDifferentAr = "تجمع بين مسار مشترك خطير يمكن للخصم فيه أسر قطعتك، وخانات 'الروزيت' الآمنة التي تمنح دوراً حاسماً ثانياً.",
            whyThisGameAr = "عمرها أكثر من 4500 عام وما زالت ممتعة وحماسية تماماً مثل أي لعبة عصرية سريعة الإيقاع.",
            modes = GameModes(
                learning = true,
                ai = AiModeConfig(available = true, difficulties = listOf("beginner", "intermediate")),
                localMultiplayer = true,
                remoteMultiplayer = false
            ),
            rules = GameRules(
                overviewAr = "يتسابق كل لاعب لتحريك 4 إلى 7 قطع عبر مسار مركب من 14 خانة وخروجها من اللوح قبل قطع الخصم.",
                steps = listOf(
                    RuleStep(
                        stepNumber = 1,
                        titleAr = "رمي النرد الهرمي",
                        detailAr = "يُرمى 4 نردات هرمية الشكل لها رأسان أبيضان. النتيجة تتراوح بين 0 و 4 حسب عدد الرؤوس البيضاء الظاهرة.",
                        tipAr = "رمية 0 تعني خسارة الدور تلقائياً."
                    ),
                    RuleStep(
                        stepNumber = 2,
                        titleAr = "الدخول والمسار",
                        detailAr = "تدخل القطع من مسار جانبي خاص، ثم تلتقي في المسار الأوسط المشترك المكون من 8 خانات حيث تشتعل المطاردة.",
                        tipAr = "الهبوط على قطعة الخصم في المسار الأوسط يعيدها فوراً إلى البداية."
                    ),
                    RuleStep(
                        stepNumber = 3,
                        titleAr = "خانة الروزيت (الوردة)",
                        detailAr = "تعتبر خانات الروزيت ملاذاً آمناً؛ لا يمكن أسر أي قطعة تقف عليها، وتمنحك دوراً إضافياً لرمي النرد مجدداً.",
                        tipAr = "حاول دائماً تثبيت قطعتك على الروزيت في منتصف اللوح لمنع تقدم الخصم."
                    )
                ),
                winCondition = "أول لاعب ينجح في إيصال جميع قطعه إلى نهاية المسار وإخراجها يفوز بالمباراة.",
                drawCondition = "لا يوجد تعادل في لعبة أور الملكية."
            ),
            tutorial = TutorialModes(
                quick = listOf(
                    TutorialStep(
                        stepIndex = 1,
                        titleAr = "الهدف",
                        explanationAr = "أخرج قطعك الأربع من اللوح قبل أن يفعل الخصم ذلك."
                    ),
                    TutorialStep(
                        stepIndex = 2,
                        titleAr = "المسار الآمن والمشترك",
                        explanationAr = "الخانات الجانبية آمنة لك وحدك، بينما الخانات الوسطى معركة مفتوحة مع الخصم."
                    )
                ),
                learn = listOf(
                    TutorialStep(
                        stepIndex = 1,
                        titleAr = "خطوة رمي النرد",
                        explanationAr = "اضغط زر 'ارمِ النرد' لملاحظة القيمة (0 إلى 4)."
                    ),
                    TutorialStep(
                        stepIndex = 2,
                        titleAr = "تحريك القطعة المناسبة",
                        explanationAr = "اختر القطعة التي تقربك من خانة الروزيت أو التي تمكّنك من أسر قطعة للخصم."
                    )
                )
            ),
            playability = PlayabilityConfig(
                engine = PlayabilityEngine.dice_engine,
                status = "custom",
                fidelity = "faithful",
                playableRoute = "/play/ur",
                ai = true,
                local = true,
                tutorial = true
            ),
            sources = listOf(
                Source(
                    sourceId = "src_finkel_2007",
                    label = "On the Rules for the Royal Game of Ur",
                    author = "Irving Finkel",
                    year = 2007,
                    publisher = "British Museum Press",
                    url = "https://www.britishmuseum.org",
                    type = SourceType.academic
                ),
                Source(
                    sourceId = "src_british_museum_ur",
                    label = "The Royal Game of Ur Collection Record",
                    author = "British Museum Department of Middle East",
                    year = 1928,
                    publisher = "British Museum",
                    url = "https://www.britishmuseum.org/collection/object/W_1928-1009-378",
                    type = SourceType.museum
                )
            ),
            imageAttribution = listOf(
                ImageAttribution(
                    source = "British Museum Open Access",
                    license = "CC BY-NC-SA 4.0",
                    creator = "The Trustees of the British Museum",
                    attributionText = "Museum number 1928,1009.378; excavated by Leonard Woolley at Ur."
                )
            ),
            credits = listOf(
                Credit(roleAr = "توثيق تاريخي", nameAr = "فريق أبحاث مِرث"),
                Credit(roleAr = "تحقيق القواعد المسمارية", nameAr = "د. إيرفينغ فينكل")
            ),
            printableBoard = PrintableBoardSpec(
                titleAr = "لوحة أور الملكية ذات الـ 20 مربعاً",
                dimensionsAr = "ورقة قياس A4 (شريط 3x8 مفرغ الخصر)",
                instructionsAr = "اطبع اللوحة، واستخدم 8 أحجار بلونين مختلفين و4 قطع نرد عادية (الزوجي = 1، الفردي = 0).",
                pieceRequirementsAr = "4 قطع بنية و4 قطع بيضاء أو ذهبية."
            ),
            relatedGames = listOf(
                RelatedGameLink(targetSlug = "senet", reasonAr = "كلاهما من أقدم ألعاب مسارات النرد والرمزية الروحية في العالم القديم.")
            ),
            audienceSuitability = listOf("families", "schools", "researchers", "history_lovers")
        ),

        Game(
            id = "game_alquerque",
            slug = "alquerque",
            titleAr = "القِرْق الأندلسي",
            titleEn = "Alquerque",
            originalTitle = "القِرق (El Alquerque)",
            aliases = listOf("قرق خمسة", "Alquerque de Doce"),
            origin = GameOrigin(
                countryId = "morocco_andalusia",
                regionId = "mediterranean",
                culture = "العالم العربي والأندلس الإسلامية",
                status = OriginStatus.documented,
                claims = listOf(
                    DisputedClaim(
                        narrativeTitleAr = "الأصول المشرقية القديمة",
                        narrativeSummaryAr = "ورد ذكر القِرق في كتاب الأغاني لأبي الفرج الأصفهاني (القرن العاشر الميلادي)، ورجح مؤرخون اشتقاقه من ألعاب رومانية شبيهة.",
                        sourceRefs = listOf("src_aghani")
                    ),
                    DisputedClaim(
                        narrativeTitleAr = "التوثيق الأندلسي في كتاب الألعاب",
                        narrativeSummaryAr = "وثّق الملك ألفونسو العاشر في إشبيلية عام 1283م تفاصيل رقعتها ذات الـ 25 نقطة وقواعد القفز والأسر الإجباري.",
                        sourceRefs = listOf("src_alfonso_x")
                    )
                )
            ),
            historicalContext = HistoricalContext(
                summaryAr = "تعتبر لعبة القرق الجد المباشر والموثق للعبة الدامة العالمية ولعبة زكي في أفريقيا. انتقلت من الأندلس إلى أرجاء أوروبا في العصور الوسطى كإحدى أبرز ألعاب المناورة التكتيكية الصرفة الخالية من الحظ.",
                era = "القرن العاشر - القرن الثالث عشر الميلادي",
                notesAr = "مخطوطة كتاب الألعاب (Libro de los juegos) المحفوظة في الإسكوريال هي المصدر البصري الأهم للعبة.",
                claims = listOf(
                    ClaimBlock(
                        claimId = "alq_c1",
                        textAr = "تطوير الدامة في جنوب فرنسا وإسبانيا في القرن الرابع عشر تم بدمج رقعة الشطرنج مع قواعد قفز القِرق.",
                        sourceRefs = listOf("src_murray_boardgames"),
                        status = ClaimStatus.supported
                    )
                )
            ),
            players = PlayerSpecs(min = 2, max = 2, teamBased = false),
            estimatedPlayTime = PlayTimeSpecs(minMinutes = 10, maxMinutes = 20),
            timeToLearnMinutes = 3,
            rulesDifficulty = 2,
            masteryDifficulty = 5,
            primaryCategory = "تطويق وقفز إستراتيجي",
            secondaryCategories = listOf("تراث أندلسي", "بدون حظ"),
            playStyle = listOf("أسر بالقفز الإجباري", "محاصرة القطع", "حساب عميق للحركات المتتالية"),
            tags = listOf("الأندلس", "القرق", "دامة", "تكتيك", "مخطوطة ألفونسو"),
            whatMakesItDifferentAr = "قاعدة الأسر بالقفز المتسلسل تجعل حركة واحدة خاطئة كفيلة بانهيار صفوف كاملة من القطع.",
            whyThisGameAr = "درس عبقري في التخطيط المستقبلي؛ لا مجال فيها للحظ مطلقاً، الفوز للأكثر بعد نظر.",
            modes = GameModes(
                learning = true,
                ai = AiModeConfig(available = true, difficulties = listOf("beginner", "expert")),
                localMultiplayer = true,
                remoteMultiplayer = false
            ),
            rules = GameRules(
                overviewAr = "تُلعب على شبكة 5x5 بها 25 نقطة تقاطع متصلة بخطوط أفقية ورأسية ومائلة. يمتلك كل لاعب 12 قطعة.",
                steps = listOf(
                    RuleStep(
                        stepNumber = 1,
                        titleAr = "وضعية البداية",
                        detailAr = "توضع القطع الـ 24 على جميع النقاط ما عدا النقطة المركزية التي تظل فارغة لبدء أول حركة.",
                        tipAr = "النقطة المركزية هي مفتاح فتح ممرات المناورة."
                    ),
                    RuleStep(
                        stepNumber = 2,
                        titleAr = "حركة المشي",
                        detailAr = "تتحرك القطعة خطوة واحدة على طول أي خط مستقيم إلى نقطة مجاورة فارغة.",
                        tipAr = "لا تتحرك القطعة إلا على مسارات الخطوط المرسومة."
                    ),
                    RuleStep(
                        stepNumber = 3,
                        titleAr = "الأسر بالقفز الإلزامي",
                        detailAr = "إذا كانت قطعة الخصم مجاورة وخلفها نقطة فارغة في نفس الخط المستقيم، يجب القفز فوقها وأسرها فوراً.",
                        tipAr = "يمكن تنفيذ قفزات متعددة في نفس الدور إذا توفرت شروط القفز المتتابع."
                    )
                ),
                winCondition = "أسر جميع قطع الخصم أو محاصرتها بحيث لا يجد أي حركة قانونية متاحة.",
                drawCondition = "التعادل إذا تكررت الحركات دون أي إمكانية للأسر لعدة أدوار متتالية."
            ),
            tutorial = TutorialModes(
                quick = listOf(
                    TutorialStep(
                        stepIndex = 1,
                        titleAr = "الهدف",
                        explanationAr = "اقفز فوق قطع الخصم لتبتلعها ونظف الرقعة بالكامل."
                    )
                ),
                learn = listOf(
                    TutorialStep(
                        stepIndex = 1,
                        titleAr = "القفز المزدوج",
                        explanationAr = "إذا هبطت قطعتك ووجدت قطعة أخرى يمكن القفز فوقها، يُكمل الدور فوراً."
                    )
                )
            ),
            playability = PlayabilityConfig(
                engine = PlayabilityEngine.alignment_engine,
                status = "custom",
                fidelity = "faithful",
                playableRoute = "/play/alquerque",
                ai = true,
                local = true,
                tutorial = true
            ),
            sources = listOf(
                Source(
                    sourceId = "src_alfonso_x",
                    label = "Libro de los juegos (Book of Games)",
                    author = "Alfonso X of Castile",
                    year = 1283,
                    publisher = "Monasterio de El Escorial, MS T.I.6",
                    type = SourceType.archive
                ),
                Source(
                    sourceId = "src_murray_boardgames",
                    label = "A History of Board-Games Other Than Chess",
                    author = "H. J. R. Murray",
                    year = 1952,
                    publisher = "Oxford University Press",
                    type = SourceType.book
                ),
                Source(
                    sourceId = "src_aghani",
                    label = "كتاب الأغاني",
                    author = "أبو الفرج الأصفهاني",
                    year = 967,
                    publisher = "دار الكتب المصرية",
                    type = SourceType.archive
                )
            ),
            imageAttribution = listOf(
                ImageAttribution(
                    source = "Real Biblioteca del Monasterio de San Lorenzo de El Escorial",
                    license = "Public Domain",
                    creator = "Royal Scribes of Alfonso X",
                    attributionText = "Illuminated miniature depicting Moorish players at an Alquerque board, folio 91v."
                )
            ),
            credits = listOf(
                Credit(roleAr = "تحقيق المخطوطة الأندلسية", nameAr = "أرشيف مِرث للمخطوطات"),
                Credit(roleAr = "صياغة القواعد التفاعلية", nameAr = "فريق التطوير الرياضي")
            ),
            printableBoard = PrintableBoardSpec(
                titleAr = "رقعة القِرق الأندلسية (5×5)",
                dimensionsAr = "ورقة A4 مربعة مع خطوط الأقطار",
                instructionsAr = "اطبع الرقعة، واستخدم 12 حجراً أسود و12 حجراً أبيض.",
                pieceRequirementsAr = "12 قطعة داكنة و 12 قطعة فاتحة."
            ),
            relatedGames = listOf(
                RelatedGameLink(targetSlug = "shisima", reasonAr = "تشترك معها في هندسة شبكات التراصف والمناورة الهندسية.")
            ),
            audienceSuitability = listOf("schools", "families", "history_lovers", "researchers")
        ),

        Game(
            id = "game_oware",
            slug = "oware",
            titleAr = "أواري (عائلة المانكالا)",
            titleEn = "Oware / Awale",
            originalTitle = "ɔware (لغة التوي / الأكان)",
            aliases = listOf("أوالي", "Awélé", "مانكالا غرب أفريقيا"),
            origin = GameOrigin(
                countryId = "ghana",
                regionId = "sub_saharan_africa",
                culture = "شعب الأكان والأشانتي (غانا وساحل العاج)",
                status = OriginStatus.documented,
                claims = emptyList()
            ),
            historicalContext = HistoricalContext(
                summaryAr = "تعتبر 'أواري' أشهر وأرقى ألعاب عائلة المانكالا الإفريقية الحسابية. يعني اسمها بلغة الأشانتي حرفياً 'لقد تزوجا' تقديراً لقضاء الأزواج ساعات طويلة من التفكير المشترك معاً أمام لوحها الخشبي.",
                era = "موثقة منذ القرن السابع عشر وموروثة شفهياً لقرون خلت",
                notesAr = "تعتمد على بذور شجرة الـ Nickar ذات الصلابة والوزن الإيقاعي الجذاب.",
                claims = listOf(
                    ClaimBlock(
                        claimId = "ow_c1",
                        textAr = "تفرض تقاليد الأكان مبدأ 'إطعام الخصم' (Grand Slam prohibition)؛ لا يجوز للاعب حصد كل بذور الخصم وتركه بلا أي حركة متاحة.",
                        sourceRefs = listOf("src_rudge_oware"),
                        status = ClaimStatus.supported
                    )
                )
            ),
            players = PlayerSpecs(min = 2, max = 2, teamBased = false),
            estimatedPlayTime = PlayTimeSpecs(minMinutes = 15, maxMinutes = 30),
            timeToLearnMinutes = 5,
            rulesDifficulty = 2,
            masteryDifficulty = 5,
            primaryCategory = "حساب إيقاعي وبذر بذور",
            secondaryCategories = listOf("تراث إفريقي", "عائلة المانكالا"),
            playStyle = listOf("توزيع إيقاعي عكس عقارب الساعة", "حصد الحفر الزوجية", "عد ذهني متقدم"),
            tags = listOf("غانا", "مانكالا", "بذور", "حساب ذهني", "تراث عالمي"),
            whatMakesItDifferentAr = "لا يوجد فيها حظ مطلقاً؛ النصر يأتي من توقع مكان استقرار الحبة الأخيرة قبل لمس الحفرة.",
            whyThisGameAr = "رياضيات نقية بأدوات طبيعية بسيطة تأسر العقول الصغار والكبار وتنمي سرعة البديهة.",
            modes = GameModes(
                learning = true,
                ai = AiModeConfig(available = true, difficulties = listOf("beginner", "intermediate", "expert")),
                localMultiplayer = true,
                remoteMultiplayer = false
            ),
            rules = GameRules(
                overviewAr = "لوح يتكون من صفين متقابلين، كل صف يحتوي 6 حفر، بإجمالي 48 بذرة (4 بذور في كل حفرة عند البداية).",
                steps = listOf(
                    RuleStep(
                        stepNumber = 1,
                        titleAr = "آلية البَذر",
                        detailAr = "يختار اللاعب حفرة من صفّه، ويلتقط جميع بذورها، ثم يوزعها حبة حبة في الحفر التالية باتجاه عكس عقارب الساعة.",
                        tipAr = "إذا احتوت الحفرة على 12 بذرة أو أكثر، يتم تجاوز حفرة الانطلاق الأصلية."
                    ),
                    RuleStep(
                        stepNumber = 2,
                        titleAr = "شروط الحَصْد (Capture)",
                        detailAr = "إذا استقرت البذرة الأخيرة في حفرة في صف الخصم وأصبح عدد البذور فيها 2 أو 3 حبات، يقوم اللاعب بحصدها فوراً.",
                        tipAr = "يمتد الحصد للحفر السابقة مباشرة إذا حققت نفس الشرط (2 أو 3)."
                    ),
                    RuleStep(
                        stepNumber = 3,
                        titleAr = "أخلاقية إطعام الخصم",
                        detailAr = "إذا كان صف الخصم فارغاً تماماً من البذور، يجب عليك اختيار حركة تمنحه بذرة واحدة على الأقل للاستمرار.",
                        tipAr = "فلسفة اللعبة ترفض القضاء التام على الخصم وتفضّل التفوق التراكمي."
                    )
                ),
                winCondition = "أول لاعب يجمع 25 بذرة في خزائنه يفوز رسمياً بالمباراة.",
                drawCondition = "التعادل إذا حصل كل لاعب على 24 بذرة بالضبط."
            ),
            tutorial = TutorialModes(
                quick = listOf(
                    TutorialStep(
                        stepIndex = 1,
                        titleAr = "الهدف",
                        explanationAr = "احصد 25 بذرة لتفوز."
                    )
                ),
                learn = listOf(
                    TutorialStep(
                        stepIndex = 1,
                        titleAr = "جرب البذر",
                        explanationAr = "اضغط على أي حفرة من حفرك لمشاهدة توزيع البذور بالتتابع."
                    )
                )
            ),
            playability = PlayabilityConfig(
                engine = PlayabilityEngine.mancala_engine,
                status = "custom",
                fidelity = "faithful",
                playableRoute = "/play/oware",
                ai = true,
                local = true,
                tutorial = true
            ),
            sources = listOf(
                Source(
                    sourceId = "src_rudge_oware",
                    label = "The Royal Art of Oware",
                    author = "Peter Rudge",
                    year = 1995,
                    publisher = "International Oware Society",
                    url = "https://www.oware.org",
                    type = SourceType.academic
                ),
                Source(
                    sourceId = "src_unesco_mancala",
                    label = "Traditional Mancala Games in African Heritage",
                    author = "UNESCO Intangible Cultural Heritage",
                    year = 2012,
                    publisher = "UNESCO",
                    type = SourceType.official
                )
            ),
            imageAttribution = listOf(
                ImageAttribution(
                    source = "Smithsonian National Museum of African Art",
                    license = "Smithsonian Open Access",
                    creator = "Ashanti Carver",
                    attributionText = "Carved hardwood Oware board with storage chamber, late 19th century."
                )
            ),
            credits = listOf(
                Credit(roleAr = "توثيق التراث الإفريقي", nameAr = "جمعية أواري الدولية"),
                Credit(roleAr = "تطوير محرك المانكالا", nameAr = "مهندسو مِرث")
            ),
            printableBoard = PrintableBoardSpec(
                titleAr = "لوحة أواري للحصاد (2×6)",
                dimensionsAr = "ورقة A4 بها 12 دائرة ومستودعان جانبيان",
                instructionsAr = "ضع 4 حبات فاصولياء أو خرز في كل دائرة من الدوائر الـ 12.",
                pieceRequirementsAr = "48 بذرة أو خرزة أو حصاة صغيرة."
            ),
            relatedGames = listOf(
                RelatedGameLink(targetSlug = "royal-game-of-ur", reasonAr = "كلاهما يعتمد على عد تراكمي تاريخي يعود لآلاف السنين.")
            ),
            audienceSuitability = listOf("schools", "families", "researchers", "history_lovers")
        ),

        Game(
            id = "game_shisima",
            slug = "shisima",
            titleAr = "شيسِيما (Shisima)",
            titleEn = "Shisima",
            originalTitle = "Shisima (لغة التيريكي / كينيا)",
            aliases = listOf("لعبة بركة الماء الكينية"),
            origin = GameOrigin(
                countryId = "kenya",
                regionId = "sub_saharan_africa",
                culture = "شعب التيريكي (غرب كينيا)",
                status = OriginStatus.documented,
                claims = emptyList()
            ),
            historicalContext = HistoricalContext(
                summaryAr = "لعبة تراصف سريعة تمارسها أجيال قبائل التيريكي في غرب كينيا. تعني كلمة 'شيسيما' بلغتهم 'مسطح الماء' أو 'البئر'، وتُشبَّه القطع بحشرات الماء (Imbalavali) التي تتحرك بسرعة فائقة نحو المركز وتتجنب أن تلتهمها حركة الخصم.",
                era = "تقليد شعبي شفهي متوارث",
                notesAr = "تُرسم رقعتها الثمانية في ثوانٍ على التراب باستخدام غصن شجرة وتلعب بـ 6 أحجار ملونة.",
                claims = listOf(
                    ClaimBlock(
                        claimId = "shi_c1",
                        textAr = "تم تسجيل اللعبة وتوثيق بيداغوجيتها الحسابية في مشاريع تدريس الرياضيات الإفريقية الشعبية من قبل كلوديا زاسلافسكي.",
                        sourceRefs = listOf("src_zaslavsky_africa"),
                        status = ClaimStatus.supported
                    )
                )
            ),
            players = PlayerSpecs(min = 2, max = 2, teamBased = false),
            estimatedPlayTime = PlayTimeSpecs(minMinutes = 5, maxMinutes = 10),
            timeToLearnMinutes = 2,
            rulesDifficulty = 1,
            masteryDifficulty = 3,
            primaryCategory = "تراصف ومطاردة سريعة",
            secondaryCategories = listOf("تراث كيني", "ألعاب تعليمية للمدارس"),
            playStyle = listOf("تراصف ثلاثي", "سيطرة على النقطة المركزية", "مباراة سريعة الخاطر"),
            tags = listOf("كينيا", "تراصف", "سرعة", "مدارس", "أطفال وعائلات"),
            whatMakesItDifferentAr = "رقعتها الثمانية الفريدة ذات النقطة المركزية الواحدة المفتوحة تجعل كل مباراة تجربة ذهنية مباغتة في أقل من دقيقتين.",
            whyThisGameAr = "أسهل مدخل للأطفال والمدارس لاكتشاف ألعاب التفكير الإفريقي وسرعة التنسيق البصري.",
            modes = GameModes(
                learning = true,
                ai = AiModeConfig(available = true, difficulties = listOf("beginner", "intermediate")),
                localMultiplayer = true,
                remoteMultiplayer = false
            ),
            rules = GameRules(
                overviewAr = "رقعة ثمانية الأضلاع بها 8 نقاط محيطية متصلة جميعها بنقطة مركزية واحدة تدعى 'شيسيما'. يمتلك كل لاعب 3 قطع.",
                steps = listOf(
                    RuleStep(
                        stepNumber = 1,
                        titleAr = "نشر القطع البدائي",
                        detailAr = "يضع كل لاعب قطعه الثلاث في ثلاث نقاط متتالية على المحيط الخارجي ويبقى المركز فارغاً.",
                        tipAr = "المركز هو شريان الحياة لكل المناورات."
                    ),
                    RuleStep(
                        stepNumber = 2,
                        titleAr = "التحريك بالتناوب",
                        detailAr = "يحرك كل لاعب قطعة واحدة إلى أي نقطة مجاورة شاغرة على المحيط أو إلى النقطة المركزية مباشرة.",
                        tipAr = "احتلال المركز يمنع خصمك من عبور خط التراصف."
                    ),
                    RuleStep(
                        stepNumber = 3,
                        titleAr = "حظر تكرار الحركات الثلاثي",
                        detailAr = "إذا كرر اللاعبون نفس الحركة ثلاث مرات دون تغيير لمنع الفوز، تُعتبر المباراة تعادلاً.",
                        tipAr = "غامر وقم بمناورة جانبية لكسر حلقة الدفاع."
                    )
                ),
                winCondition = "أول لاعب يصنع صفاً مستقيماً من قطعه الثلاث يمر بالمركز يفوز فوراً.",
                drawCondition = "التعادل باتفاق الطرفين أو عند تكرار الحركات المتماثلة."
            ),
            tutorial = TutorialModes(
                quick = listOf(
                    TutorialStep(
                        stepIndex = 1,
                        titleAr = "الهدف المباشر",
                        explanationAr = "اصنع خطاً مستقيماً من 3 قطع يقطع المركز."
                    )
                )
            ),
            playability = PlayabilityConfig(
                engine = PlayabilityEngine.alignment_engine,
                status = "custom",
                fidelity = "faithful",
                playableRoute = "/play/shisima",
                ai = true,
                local = true,
                tutorial = true
            ),
            sources = listOf(
                Source(
                    sourceId = "src_zaslavsky_africa",
                    label = "Africa Counts: Number and Pattern in African Cultures",
                    author = "Claudia Zaslavsky",
                    year = 1973,
                    publisher = "Prindle, Weber & Schmidt",
                    type = SourceType.academic
                )
            ),
            imageAttribution = listOf(
                ImageAttribution(
                    source = "Mirath Archive Cultural Diagrams",
                    license = "Creative Commons CC-BY 4.0",
                    creator = "Mirath Cartography Team",
                    attributionText = "Geometrical reconstruction of the Octagonal Shisima Water-Pool board."
                )
            ),
            credits = listOf(
                Credit(roleAr = "توثيق الأنماط الإفريقية", nameAr = "كلوديا زاسلافسكي"),
                Credit(roleAr = "تصميم الرقعة التفاعلية", nameAr = "فريق مِرث")
            ),
            printableBoard = PrintableBoardSpec(
                titleAr = "رقعة شيسيما الثمانية (Shisima Board)",
                dimensionsAr = "صفحة A4 ثماني أضلاع بمركز دائري",
                instructionsAr = "اطبع الرقعة، وضع 3 عملات معدنية بلون و3 بلون آخر على المحيط الخارجي.",
                pieceRequirementsAr = "3 أحجار حمراء و 3 أحجار بيضاء."
            ),
            relatedGames = listOf(
                RelatedGameLink(targetSlug = "alquerque", reasonAr = "كلاهما يعتمد على تراصف ومناورة القطع الهندسية بدون أي نرد.")
            ),
            audienceSuitability = listOf("schools", "families", "history_lovers")
        )
    )

    fun getGameBySlug(slug: String): Game? = games.find { it.slug == slug }
    fun getCountryById(id: String): Country? = countries.find { it.id == id }
    fun getRegionById(id: String): Region? = regions.find { it.id == id }
}
