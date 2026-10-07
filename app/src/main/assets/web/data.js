/**
 * Mirath Historical Repository & Data Models (Browser Replica)
 */
const MirathData = {
  regions: [
    {
      id: "middle_east",
      nameAr: "الشرق الأوسط وشمال أفريقيا",
      nameEn: "Middle East & North Africa",
      summaryAr: "مهد أقدم ألعاب الألواح والسباق الإستراتيجي، من بلاد الرافدين ووادي النيل إلى شبه الجزيرة العربية."
    },
    {
      id: "sub_saharan_africa",
      nameAr: "أفريقيا جنوب الصحراء",
      nameEn: "Sub-Saharan Africa",
      summaryAr: "موطن أعرق ألعاب الحصاد والتوزيع الإيقاعي كعائلة المانكالا والألواح الرملية المنحوتة."
    },
    {
      id: "mediterranean",
      nameAr: "حوض البحر الأبيض المتوسط",
      nameEn: "Mediterranean",
      summaryAr: "ملتقى الثقافات الإيبيرية والرومانية والإغريقية، موطن ألعاب المحاصرة والتراصف التكتيكي."
    },
    {
      id: "east_asia",
      nameAr: "شرق آسيا",
      nameEn: "East Asia",
      summaryAr: "ألعاب التوازن الفلسفي وحصار المساحات والتطويق الصامت الموثقة في المخطوطات الملكية."
    },
    {
      id: "scandinavia",
      nameAr: "شمال أوروبا والدول الإسكندنافية",
      nameEn: "Northern Europe & Scandinavia",
      summaryAr: "ألعاب غير متناظرة تحاكي غارات الملوك ومحاولات الهروب التكتيكية فوق شبكات خشبية."
    }
  ],

  countries: [
    {
      id: "iraq",
      nameAr: "العراق (بلاد الرافدين)",
      nameEn: "Iraq (Mesopotamia)",
      regionId: "middle_east",
      summaryAr: "عُثر في مقابر أور الملكية على أقدم ألواح اللعب المتكاملة في التاريخ الإنساني.",
      historicalGamingCultureAr: "ارتبطت الألعاب بالتكهن والرمزية الدينية وحظوظ القصور السومرية والأكادية."
    },
    {
      id: "egypt",
      nameAr: "مصر القديمة",
      nameEn: "Ancient Egypt",
      regionId: "middle_east",
      summaryAr: "وثّقت الجداريات الفرعونية ألعاب السباق والعبور إلى الحياة الأخرى كالسنت ومحاصرة الكلاب والضباع.",
      historicalGamingCultureAr: "كانت ألعاب الألواح جزءاً من الطقوس الجنائزية والترويح البلاطي."
    },
    {
      id: "kenya",
      nameAr: "كينيا وشرق أفريقيا",
      nameEn: "Kenya & East Africa",
      regionId: "sub_saharan_africa",
      summaryAr: "شهدت ألعاب التراصف وتوزيع البذور حيوية مجتمعية شعبية متوارثة شفهياً بين الأجيال.",
      historicalGamingCultureAr: "تُمارس في الساحات العامة والمدارس لتعزيز الحساب الذهني السريع والتركيز الإيقاعي."
    },
    {
      id: "morocco_andalusia",
      nameAr: "المغرب والأندلس",
      nameEn: "Morocco & Al-Andalus",
      regionId: "mediterranean",
      summaryAr: "وثّق كتاب الألعاب للملك ألفونسو العاشر (1283م) انتقال ألعاب القرق والتربيع الإسلامية إلى أوروبا.",
      historicalGamingCultureAr: "تفاعل تكتيكي عميق بين الألعاب الاستراتيجية العربية والتقاليد المتوسطية."
    },
    {
      id: "ghana",
      nameAr: "غانا وغرب أفريقيا",
      nameEn: "Ghana & West Africa",
      regionId: "sub_saharan_africa",
      summaryAr: "تُعتبر لعبة 'أواري' إحدى أعرق ألعاب الذكاء التي نُحتت ألواحها على الخشب الصلب كرموز مكانة وتواصل.",
      historicalGamingCultureAr: "تُقام لها بطولات رسمية في الثقافة الأكانية لتربية قادة المجتمع على إدارة الموارد بحكمة."
    },
    {
      id: "scandinavia_region",
      nameAr: "إسكندنافيا (عصر الفايكنغ)",
      nameEn: "Scandinavia",
      regionId: "scandinavia",
      summaryAr: "طور الإسكندنافيون ألعاب التافيل (Tafl) الملكية غير المتناظرة للدفاع عن الملك وتطويقه.",
      historicalGamingCultureAr: "كانت وسيلة رئيسية لتدريب المحاربين على التخطيط الحربي في الليالي القطبية الطويلة."
    }
  ],

  glossary: [
    {
      id: "mancala_sowing",
      slug: "mancala-sowing",
      termAr: "البَذْر (Sowing)",
      definitionAr: "آلية التقاط جميع البذور من حفرة معينة وتوزيعها بالتتابع واحدة تلو الأخرى في الحفر التالية باتجاه محدد.",
      originLanguageAr: "العربية (نَقَلَ / بَذَرَ) والسواحيلية",
      relatedGamesSlugs: ["oware"]
    },
    {
      id: "alquerque_leap",
      slug: "alquerque-leap",
      termAr: "القفز والأسر الإجباري (Leap Capture)",
      definitionAr: "قاعدة تحتم القفز فوق قطعة الخصم إلى خانة فارغة تليها مباشرة لإزالتها، وهي الجد التاريخي للعبة الدامة الحديثة.",
      originLanguageAr: "العربية (القِرق)",
      relatedGamesSlugs: ["alquerque"]
    },
    {
      id: "rosette_safe",
      slug: "rosette-safe",
      termAr: "الروزيت (Rosette Square)",
      definitionAr: "خانة الوردة الثمانية المنقوشة في لوح أور، وتمنح اللاعب حصانة تامة من الأسر بالإضافة إلى دور إضافي لرمي النرد.",
      originLanguageAr: "السومرية والبابلية القديمة",
      relatedGamesSlugs: ["royal-game-of-ur"]
    },
    {
      id: "shisima_center",
      slug: "shisima-center",
      termAr: "شيسِيما (Water Pool)",
      definitionAr: "النقطة المركزية في الرقعة الثمانية التي ترمز إلى بركة الماء، والتي يجب أن يمر بها خط الفوز الثلاثي.",
      originLanguageAr: "لغة التيريكي (كينيا)",
      relatedGamesSlugs: ["shisima"]
    }
  ],

  games: [
    {
      id: "game_ur",
      slug: "royal-game-of-ur",
      titleAr: "لعبة أور الملكية",
      titleEn: "Royal Game of Ur",
      originalTitle: "Lu-malku (سومري مفترض)",
      aliases: ["لعبة العشرين مربعاً", "Game of Twenty Squares"],
      countryId: "iraq",
      regionId: "middle_east",
      culture: "الحضارة السومرية - بلاد الرافدين",
      era: "2600 ق.م - القرن الثاني ق.م",
      summaryAr: "اكتشف عالم الآثار السير ليونارد وولي الألواح الملكية الفاخرة المطعمة بالعاج واللازورد في مقابر أور الملكية (حوالي 2600 قبل الميلاد). وفك الباحث إيرفينغ فينكل شفرة قواعدها من لوح مسماري بابلي يعود للعام 177 قبل الميلاد.",
      rulesDifficulty: 2,
      masteryDifficulty: 4,
      primaryCategory: "سباق وتكتيك مسار",
      minPlayers: 2,
      maxPlayers: 2,
      playTime: "15-25 دقيقة",
      engine: "ur",
      tags: ["أور", "سومر", "متحف بريطاني", "نرد", "لوح خشبي"],
      audienceSuitability: ["families", "schools", "researchers", "history_lovers"],
      whatMakesItDifferentAr: "تجمع بين مسار مشترك خطير يمكن للخصم فيه أسر قطعتك، وخانات 'الروزيت' الآمنة التي تمنح دوراً حاسماً ثانياً.",
      whyThisGameAr: "عمرها أكثر من 4500 عام وما زالت ممتعة وحماسية تماماً مثل أي لعبة عصرية سريعة الإيقاع.",
      steps: [
        {
          num: 1,
          title: "رمي النرد الهرمي",
          detail: "يُرمى 4 نردات هرمية الشكل لها رأسان أبيضان. النتيجة تتراوح بين 0 و 4 حسب عدد الرؤوس البيضاء الظاهرة.",
          tip: "رمية 0 تعني خسارة الدور تلقائياً."
        },
        {
          num: 2,
          title: "الدخول والمسار",
          detail: "تدخل القطع من مسار جانبي خاص، ثم تلتقي في المسار الأوسط المشترك المكون من 8 خانات حيث تشتعل المطاردة.",
          tip: "الهبوط على قطعة الخصم في المسار الأوسط يعيدها فوراً إلى البداية."
        },
        {
          num: 3,
          title: "خانة الروزيت (الوردة)",
          detail: "تعتبر خانات الروزيت ملاذاً آمناً؛ لا يمكن أسر أي قطعة تقف عليها، وتمنحك دوراً إضافياً لرمي النرد مجدداً.",
          tip: "حاول دائماً تثبيت قطعتك على الروزيت في منتصف اللوح لمنع تقدم الخصم."
        }
      ],
      winCondition: "أول لاعب ينجح في إيصال جميع قطعه إلى نهاية المسار وإخراجها يفوز بالمباراة.",
      claims: [
        {
          textAr: "تطابقت نصوص لوح الكاتب إيتي-مردوخ-بالاطو المسماري مع قواعد حركة القطع ورمي النرد الهرمي رباعي الأوجه.",
          source: "Irving Finkel (2007) - British Museum Press"
        },
        {
          textAr: "تم العثور على نقش لنفس اللوح منحوتاً على جدار حارس ثور مجنح في قصر خورساباد الأشوري منقوشاً بخربشات جنود.",
          source: "British Museum Collection Record (1928)"
        }
      ],
      sources: [
        { label: "On the Rules for the Royal Game of Ur", author: "Irving Finkel", year: 2007, publisher: "British Museum Press" },
        { label: "The Royal Game of Ur Collection Record", author: "British Museum Department of Middle East", year: 1928 }
      ],
      printableBoard: {
        title: "لوحة أور الملكية ذات الـ 20 مربعاً",
        dimensions: "ورقة قياس A4 (شريط 3x8 مفرغ الخصر)",
        instructions: "اطبع اللوحة، واستخدم 4 قطع بنية و4 قطع بيضاء و4 نردات هرمية."
      }
    },

    {
      id: "game_alquerque",
      slug: "alquerque",
      titleAr: "القِرْق الأندلسي",
      titleEn: "Alquerque",
      originalTitle: "القِرق (El Alquerque)",
      aliases: ["قرق خمسة", "Alquerque de Doce"],
      countryId: "morocco_andalusia",
      regionId: "mediterranean",
      culture: "العالم العربي والأندلس الإسلامية",
      era: "القرن العاشر - القرن الثالث عشر الميلادي",
      summaryAr: "تعتبر لعبة القرق الجد المباشر والموثق للعبة الدامة العالمية ولعبة زكي في أفريقيا. انتقلت من الأندلس إلى أرجاء أوروبا في العصور الوسطى كإحدى أبرز ألعاب المناورة التكتيكية الصرفة الخالية من الحظ.",
      rulesDifficulty: 2,
      masteryDifficulty: 5,
      primaryCategory: "تطويق وقفز إستراتيجي",
      minPlayers: 2,
      maxPlayers: 2,
      playTime: "10-20 دقيقة",
      engine: "alquerque",
      tags: ["الأندلس", "القرق", "دامة", "تكتيك", "مخطوطة ألفونسو"],
      audienceSuitability: ["schools", "families", "history_lovers", "researchers"],
      whatMakesItDifferentAr: "قاعدة الأسر بالقفز المتسلسل تجعل حركة واحدة خاطئة كفيلة بانهيار صفوف كاملة من القطع.",
      whyThisGameAr: "درس عبقري في التخطيط المستقبلي؛ لا مجال فيها للحظ مطلقاً، الفوز للأكثر بعد نظر.",
      steps: [
        {
          num: 1,
          title: "وضعية البداية",
          detail: "توضع القطع الـ 24 على جميع النقاط ما عدا النقطة المركزية التي تظل فارغة لبدء أول حركة.",
          tip: "النقطة المركزية هي مفتاح فتح ممرات المناورة."
        },
        {
          num: 2,
          title: "حركة المشي",
          detail: "تتحرك القطعة خطوة واحدة على طول أي خط مستقيم إلى نقطة مجاورة فارغة.",
          tip: "لا تتحرك القطعة إلا على مسارات الخطوط المرسومة."
        },
        {
          num: 3,
          title: "الأسر بالقفز الإلزامي",
          detail: "إذا كانت قطعة الخصم مجاورة وخلفها نقطة فارغة في نفس الخط المستقيم، يجب القفز فوقها وأسرها فوراً.",
          tip: "يمكن تنفيذ قفزات متعددة في نفس الدور إذا توفرت شروط القفز المتتابع."
        }
      ],
      winCondition: "أسر جميع قطع الخصم أو محاصرتها بحيث لا يجد أي حركة قانونية متاحة.",
      claims: [
        {
          textAr: "ورد ذكر القِرق في كتاب الأغاني لأبي الفرج الأصفهاني (القرن العاشر الميلادي)، ورجح مؤرخون اشتقاقه من ألعاب مشرقية قديمة.",
          source: "كتاب الأغاني لأبي الفرج الأصفهاني (967م)"
        },
        {
          textAr: "وثّق الملك ألفونسو العاشر في إشبيلية عام 1283م تفاصيل رقعتها ذات الـ 25 نقطة وقواعد القفز في مخطوطة الإسكوريال.",
          source: "Libro de los juegos (Alfonso X, 1283)"
        }
      ],
      sources: [
        { label: "Libro de los juegos", author: "Alfonso X of Castile", year: 1283, publisher: "Monasterio de El Escorial" },
        { label: "A History of Board-Games Other Than Chess", author: "H. J. R. Murray", year: 1952, publisher: "Oxford University Press" }
      ],
      printableBoard: {
        title: "رقعة القِرق الأندلسية (5×5)",
        dimensions: "ورقة A4 مربعة مع خطوط الأقطار",
        instructions: "اطبع الرقعة، واستخدم 12 حجراً أسود و12 حجراً أبيض."
      }
    },

    {
      id: "game_oware",
      slug: "oware",
      titleAr: "أواري (عائلة المانكالا)",
      titleEn: "Oware / Awale",
      originalTitle: "ɔware (لغة التوي / الأكان)",
      aliases: ["أوالي", "Awélé", "مانكالا غرب أفريقيا"],
      countryId: "ghana",
      regionId: "sub_saharan_africa",
      culture: "شعب الأكان والأشانتي (غانا وساحل العاج)",
      era: "موثقة منذ القرن السابع عشر وموروثة شفهياً لقرون خلت",
      summaryAr: "تعتبر 'أواري' أشهر وأرقى ألعاب عائلة المانكالا الإفريقية الحسابية. يعني اسمها بلغة الأشانتي حرفياً 'لقد تزوجا' تقديراً لقضاء الأزواج ساعات طويلة من التفكير المشترك معاً أمام لوحها الخشبي.",
      rulesDifficulty: 2,
      masteryDifficulty: 5,
      primaryCategory: "حساب إيقاعي وبذر بذور",
      minPlayers: 2,
      maxPlayers: 2,
      playTime: "15-30 دقيقة",
      engine: "oware",
      tags: ["غانا", "مانكالا", "بذور", "حساب ذهني", "تراث عالمي"],
      audienceSuitability: ["schools", "families", "researchers", "history_lovers"],
      whatMakesItDifferentAr: "لا يوجد فيها حظ مطلقاً؛ النصر يأتي من توقع مكان استقرار الحبة الأخيرة قبل لمس الحفرة.",
      whyThisGameAr: "رياضيات نقية بأدوات طبيعية بسيطة تأسر العقول الصغار والكبار وتنمي سرعة البديهة.",
      steps: [
        {
          num: 1,
          title: "آلية البَذر",
          detail: "يختار اللاعب حفرة من صفّه، ويلتقط جميع بذورها، ثم يوزعها حبة حبة في الحفر التالية باتجاه عكس عقارب الساعة.",
          tip: "إذا احتوت الحفرة على 12 بذرة أو أكثر، يتم تجاوز حفرة الانطلاق الأصلية."
        },
        {
          num: 2,
          title: "شروط الحَصْد (Capture)",
          detail: "إذا استقرت البذرة الأخيرة في حفرة في صف الخصم وأصبح عدد البذور فيها 2 أو 3 حبات، يقوم اللاعب بحصدها فوراً.",
          tip: "يمتد الحصد للحفر السابقة مباشرة إذا حققت نفس الشرط (2 أو 3)."
        },
        {
          num: 3,
          title: "أخلاقية إطعام الخصم",
          detail: "إذا كان صف الخصم فارغاً تماماً من البذور، يجب عليك اختيار حركة تمنحه بذرة واحدة على الأقل للاستمرار.",
          tip: "فلسفة اللعبة ترفض القضاء التام على الخصم وتفضّل التفوق التراكمي."
        }
      ],
      winCondition: "أول لاعب يجمع 25 بذرة في خزائنه يفوز رسمياً بالمباراة.",
      claims: [
        {
          textAr: "تفرض تقاليد الأكان مبدأ 'إطعام الخصم' (Grand Slam prohibition)؛ لا يجوز للاعب حصد كل بذور الخصم وتركه بلا أي حركة متاحة.",
          source: "Peter Rudge (1995) - International Oware Society"
        }
      ],
      sources: [
        { label: "The Royal Art of Oware", author: "Peter Rudge", year: 1995, publisher: "International Oware Society" },
        { label: "Traditional Mancala Games in African Heritage", author: "UNESCO", year: 2012 }
      ],
      printableBoard: {
        title: "لوحة أواري للحصاد (2×6)",
        dimensions: "ورقة A4 بها 12 دائرة ومستودعان جانبيان",
        instructions: "ضع 4 حبات فاصولياء أو خرز في كل دائرة من الدوائر الـ 12 بإجمالي 48 بذرة."
      }
    },

    {
      id: "game_shisima",
      slug: "shisima",
      titleAr: "شيسِيما (Shisima)",
      titleEn: "Shisima",
      originalTitle: "Shisima (لغة التيريكي / كينيا)",
      aliases: ["لعبة بركة الماء الكينية"],
      countryId: "kenya",
      regionId: "sub_saharan_africa",
      culture: "شعب التيريكي (غرب كينيا)",
      era: "تقليد شعبي شفهي متوارث",
      summaryAr: "لعبة تراصف سريعة تمارسها أجيال قبائل التيريكي في غرب كينيا. تعني كلمة 'شيسيما' بلغتهم 'مسطح الماء' أو 'البئر'، وتُشبَّه القطع بحشرات الماء التي تتحرك بسرعة فائقة نحو المركز.",
      rulesDifficulty: 1,
      masteryDifficulty: 3,
      primaryCategory: "تراصف ومطاردة سريعة",
      minPlayers: 2,
      maxPlayers: 2,
      playTime: "5-10 دقائق",
      engine: "shisima",
      tags: ["كينيا", "تراصف", "سرعة", "مدارس", "أطفال وعائلات"],
      audienceSuitability: ["schools", "families", "history_lovers"],
      whatMakesItDifferentAr: "رقعتها الثمانية الفريدة ذات النقطة المركزية الواحدة المفتوحة تجعل كل مباراة تجربة ذهنية مباغتة في أقل من دقيقتين.",
      whyThisGameAr: "أسهل مدخل للأطفال والمدارس لاكتشاف ألعاب التفكير الإفريقي وسرعة التنسيق البصري.",
      steps: [
        {
          num: 1,
          title: "نشر القطع البدائي",
          detail: "يضع كل لاعب قطعه الثلاث في ثلاث نقاط متتالية على المحيط الخارجي ويبقى المركز فارغاً.",
          tip: "المركز هو شريان الحياة لكل المناورات."
        },
        {
          num: 2,
          title: "التحريك بالتناوب",
          detail: "يحرك كل لاعب قطعة واحدة إلى أي نقطة مجاورة شاغرة على المحيط أو إلى النقطة المركزية مباشرة.",
          tip: "احتلال المركز يمنع خصمك من عبور خط التراصف."
        },
        {
          num: 3,
          title: "حظر تكرار الحركات الثلاثي",
          detail: "إذا كرر اللاعبون نفس الحركة ثلاث مرات دون تغيير لمنع الفوز، تُعتبر المباراة تعادلاً.",
          tip: "غامر وقم بمناورة جانبية لكسر حلقة الدفاع."
        }
      ],
      winCondition: "أول لاعب يصنع صفاً مستقيماً من قطعه الثلاث يمر بالمركز يفوز فوراً.",
      claims: [
        {
          textAr: "تم تسجيل اللعبة وتوثيق بيداغوجيتها الحسابية في مشاريع تدريس الرياضيات الإفريقية الشعبية من قبل كلوديا زاسلافسكي.",
          source: "Claudia Zaslavsky (1973) - Africa Counts"
        }
      ],
      sources: [
        { label: "Africa Counts: Number and Pattern in African Cultures", author: "Claudia Zaslavsky", year: 1973, publisher: "Prindle, Weber & Schmidt" }
      ],
      printableBoard: {
        title: "رقعة شيسيما الثمانية (Shisima Board)",
        dimensions: "صفحة A4 ثماني أضلاع بمركز دائري",
        instructions: "ضع 3 عملات معدنية بلون و3 بلون آخر على المحيط الخارجي."
      }
    }
  ],

  getGameBySlug(slug) {
    return this.games.find(g => g.slug === slug);
  },
  getCountryById(id) {
    return this.countries.find(c => c.id === id);
  },
  getRegionById(id) {
    return this.regions.find(r => r.id === id);
  }
};
