/**
 * Mirath Web Application - Single Page Application Orchestrator
 */

class MirathApp {
  constructor() {
    this.currentView = "home";
    this.currentParams = {};
    this.isResearcherMode = false;
    this.isAudioEnabled = true;
    this.selectedAudience = "all";
    this.searchQuery = "";
    this.selectedCategory = "all";
    this.favorites = JSON.parse(localStorage.getItem("mirath_favs") || "[]");
    this.isDarkMode = localStorage.getItem("mirath_dark_mode") === "true";
    if (this.isDarkMode) {
      document.documentElement.setAttribute("data-theme", "dark");
    }

    // Active Game Engines
    this.urEngine = new UrGameEngine();
    this.alquerqueEngine = new AlquerqueEngine();
    this.owareEngine = new OwareEngine();
    this.shisimaEngine = new ShisimaEngine();
    this.activePlayMode = "local"; // "local" or "ai"

    this.initAudio();
    this.initElements();
    this.initRouter();
  }

  initElements() {
    this.viewport = document.getElementById("app-viewport");
    this.pageTitle = document.getElementById("page-title");
    this.pageSubtitle = document.getElementById("page-subtitle");
    this.btnBack = document.getElementById("btn-back");
    this.btnResearcher = document.getElementById("btn-researcher-toggle");
    this.btnAdmin = document.getElementById("btn-admin-portal");
    this.btnTheme = document.getElementById("btn-theme-toggle");
    this.themeIcon = document.getElementById("theme-icon");
    this.btnAudio = document.getElementById("btn-audio-toggle");
    this.audioIcon = document.getElementById("audio-icon");
    this.snackbar = document.getElementById("snackbar");

    if (this.themeIcon) {
      this.themeIcon.textContent = this.isDarkMode ? "☀️" : "🌙";
    }

    // Event listeners
    this.btnBack.addEventListener("click", () => window.history.back());

    if (this.btnTheme) {
      this.btnTheme.addEventListener("click", () => this.toggleTheme());
    }

    this.btnResearcher.addEventListener("click", () => {
      this.isResearcherMode = !this.isResearcherMode;
      this.btnResearcher.classList.toggle("active", this.isResearcherMode);
      this.showSnackbar(this.isResearcherMode ? "🔬 تم تفعيل نمط الباحث والمراجع التاريخية" : "تم إلغاء نمط الباحث");
      this.render();
    });

    this.btnAdmin.addEventListener("click", () => {
      window.location.hash = "#admin";
    });

    this.btnAudio.addEventListener("click", () => {
      this.isAudioEnabled = !this.isAudioEnabled;
      this.audioIcon.textContent = this.isAudioEnabled ? "🔊" : "🔇";
      this.showSnackbar(this.isAudioEnabled ? "تم تفعيل الصوت" : "تم كتم الصوت");
    });

    document.getElementById("btn-export-json-footer").addEventListener("click", () => this.exportJsonCatalog());
    document.getElementById("btn-print-active-board").addEventListener("click", () => window.print());
  }

  toggleTheme(force) {
    this.isDarkMode = typeof force === "boolean" ? force : !this.isDarkMode;
    localStorage.setItem("mirath_dark_mode", this.isDarkMode);
    if (this.isDarkMode) {
      document.documentElement.setAttribute("data-theme", "dark");
    } else {
      document.documentElement.removeAttribute("data-theme");
    }
    if (this.themeIcon) {
      this.themeIcon.textContent = this.isDarkMode ? "☀️" : "🌙";
    }
    const adminSwitch = document.getElementById("admin-dark-mode-switch");
    if (adminSwitch) {
      adminSwitch.checked = this.isDarkMode;
    }
    this.showSnackbar(this.isDarkMode ? "🌙 تم تفعيل الوضع الليلي (المخطوطات القديمة)" : "☀️ تم تفعيل الوضع النهاري");
  }

  initAudio() {
    try {
      this.audioCtx = new (window.AudioContext || window.webkitAudioContext)();
    } catch (e) {
      this.audioCtx = null;
    }
  }

  playSound(type) {
    if (!this.isAudioEnabled || !this.audioCtx) return;
    try {
      const osc = this.audioCtx.createOscillator();
      const gain = this.audioCtx.createGain();
      osc.connect(gain);
      gain.connect(this.audioCtx.destination);

      if (type === "click") {
        osc.frequency.setValueAtTime(420, this.audioCtx.currentTime);
        gain.gain.setValueAtTime(0.12, this.audioCtx.currentTime);
        gain.gain.exponentialRampToValueAtTime(0.01, this.audioCtx.currentTime + 0.08);
        osc.start();
        osc.stop(this.audioCtx.currentTime + 0.08);
      } else if (type === "dice") {
        osc.type = "sawtooth";
        osc.frequency.setValueAtTime(220, this.audioCtx.currentTime);
        osc.frequency.exponentialRampToValueAtTime(560, this.audioCtx.currentTime + 0.15);
        gain.gain.setValueAtTime(0.15, this.audioCtx.currentTime);
        gain.gain.exponentialRampToValueAtTime(0.01, this.audioCtx.currentTime + 0.15);
        osc.start();
        osc.stop(this.audioCtx.currentTime + 0.15);
      } else if (type === "capture") {
        osc.type = "square";
        osc.frequency.setValueAtTime(600, this.audioCtx.currentTime);
        osc.frequency.exponentialRampToValueAtTime(150, this.audioCtx.currentTime + 0.2);
        gain.gain.setValueAtTime(0.18, this.audioCtx.currentTime);
        gain.gain.exponentialRampToValueAtTime(0.01, this.audioCtx.currentTime + 0.2);
        osc.start();
        osc.stop(this.audioCtx.currentTime + 0.2);
      } else if (type === "win") {
        [523.25, 659.25, 783.99, 1046.50].forEach((freq, idx) => {
          const o = this.audioCtx.createOscillator();
          const g = this.audioCtx.createGain();
          o.connect(g);
          g.connect(this.audioCtx.destination);
          o.frequency.value = freq;
          const startTime = this.audioCtx.currentTime + idx * 0.12;
          g.gain.setValueAtTime(0.15, startTime);
          g.gain.exponentialRampToValueAtTime(0.001, startTime + 0.4);
          o.start(startTime);
          o.stop(startTime + 0.4);
        });
      }
    } catch (e) {
      // Audio not supported or blocked
    }
  }

  initRouter() {
    window.addEventListener("hashchange", () => this.handleRoute());
    this.handleRoute();
  }

  handleRoute() {
    const hash = window.location.hash.slice(1) || "home";
    const parts = hash.split("/");
    const view = parts[0] || "home";
    const param = parts[1] || null;

    this.currentView = view;
    this.currentParams = { slug: param };

    // Update active nav links
    document.querySelectorAll(".nav-link, .mobile-nav-item").forEach(el => {
      el.classList.toggle("active", el.dataset.view === view);
    });

    this.render();
  }

  render() {
    this.btnBack.classList.toggle("hidden", this.currentView === "home");

    switch (this.currentView) {
      case "home":
        this.renderHome();
        break;
      case "catalog":
        this.renderCatalog();
        break;
      case "game":
        this.renderGameDetail(this.currentParams.slug);
        break;
      case "play":
        this.renderPlayGame(this.currentParams.slug);
        break;
      case "map":
        this.renderMap();
        break;
      case "glossary":
        this.renderGlossary();
        break;
      case "suggest":
        this.renderSuggest();
        break;
      case "admin":
        this.renderAdmin();
        break;
      default:
        this.renderHome();
    }
  }

  showSnackbar(msg) {
    this.snackbar.textContent = msg;
    this.snackbar.classList.remove("hidden");
    setTimeout(() => this.snackbar.classList.add("hidden"), 3200);
  }

  toggleFavorite(slug) {
    if (this.favorites.includes(slug)) {
      this.favorites = this.favorites.filter(s => s !== slug);
      this.showSnackbar("تمت إزالة اللعبة من المفضلة");
    } else {
      this.favorites.push(slug);
      this.showSnackbar("تمت إضافة اللعبة إلى المفضلة ❤️");
    }
    localStorage.setItem("mirath_favs", JSON.stringify(this.favorites));
    this.playSound("click");
    this.render();
  }

  normalizeArabic(str) {
    if (!str) return "";
    return str
      .replace(/[إأآا]/g, "ا")
      .replace(/ة/g, "ه")
      .replace(/ى/g, "ي")
      .replace(/[\u064B-\u065F]/g, "") // remove tashkeel
      .toLowerCase();
  }

  // ========================================================
  // VIEW: HOME
  // ========================================================
  renderHome() {
    this.pageTitle.textContent = "مِرث | Mirath";
    this.pageSubtitle.textContent = "ألعاب العالم المنسية";

    const filteredGames = MirathData.games.filter(g => {
      if (this.selectedAudience === "all") return true;
      return g.audienceSuitability.includes(this.selectedAudience);
    });

    this.viewport.innerHTML = `
      <!-- Hero Banner -->
      <section class="hero-banner">
        <span class="hero-tag">توثيق حي وتراث عالمي</span>
        <h2 class="hero-title">ألعاب العالم المنسية الموثقة عبر التاريخ</h2>
        <p class="hero-desc">
          منصة تفاعلية عربية تعيد إحياء ألعاب الألواح والمناورات الشعبية من الحضارات السومرية والأندلسية والإفريقية وفق قواعدها الحقيقية المدونة في المخطوطات والآثار.
        </p>
        <div class="hero-actions">
          <a href="#catalog" class="btn-primary">🎲 استكشف كتالوج الألعاب</a>
          <button id="btn-surprise-me" class="btn-secondary">✨ فاجئني بلعبة عشوائية</button>
          <a href="#suggest" class="btn-secondary">✍️ اقترح لعبة للتحقيق</a>
        </div>
      </section>

      <!-- Stats Bar -->
      <div class="stats-strip">
        <div class="stat-card">
          <span class="stat-icon">🏺</span>
          <div>
            <div class="stat-num">${MirathData.games.length}</div>
            <div class="stat-label">ألعاب تراثية محققة</div>
          </div>
        </div>
        <div class="stat-card">
          <span class="stat-icon">🗺️</span>
          <div>
            <div class="stat-num">${MirathData.regions.length}</div>
            <div class="stat-label">أقاليم وحضارات قديمة</div>
          </div>
        </div>
        <div class="stat-card">
          <span class="stat-icon">📜</span>
          <div>
            <div class="stat-num">0%</div>
            <div class="stat-label">اختلاق وتزييف للقواعد</div>
          </div>
        </div>
        <div class="stat-card">
          <span class="stat-icon">🛡️</span>
          <div>
            <div class="stat-num">100%</div>
            <div class="stat-label">أكاديمية ومصادر موثقة</div>
          </div>
        </div>
      </div>

      <!-- Audience Filter Chips -->
      <div class="section-header">
        <h3 class="section-title">اختر التجربة المناسبة لك</h3>
      </div>
      <div class="filter-scroll-bar">
        <button class="filter-chip ${this.selectedAudience === 'all' ? 'active' : ''}" data-audience="all">🌐 الكل</button>
        <button class="filter-chip ${this.selectedAudience === 'families' ? 'active' : ''}" data-audience="families">👨‍👩‍👧‍👦 العائلات</button>
        <button class="filter-chip ${this.selectedAudience === 'schools' ? 'active' : ''}" data-audience="schools">🏫 المدارس والتعليم</button>
        <button class="filter-chip ${this.selectedAudience === 'researchers' ? 'active' : ''}" data-audience="researchers">🔬 الباحثون والتاريخ</button>
        <button class="filter-chip ${this.selectedAudience === 'history_lovers' ? 'active' : ''}" data-audience="history_lovers">⚔️ ألعاب التكتيك</button>
      </div>

      <!-- Interactive D3 Geographic Regions Chart Section -->
      <section class="doc-section" style="margin-bottom: 28px; background: linear-gradient(135deg, #FAF7F0, #F4EFE6); border: 1.5px solid var(--mirath-card-border); border-radius: 20px; padding: 24px;">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px; flex-wrap: wrap; gap: 8px;">
          <div>
            <h3 style="font-size: 1.35rem; font-weight: 800; color: var(--mirath-terracotta-dark);">
              📊 توزيع الألعاب الموثقة حسب الأقاليم (Interactive D3 Chart)
            </h3>
            <p style="font-size: 0.88rem; color: var(--mirath-muted-brown);">
              رسم بياني تفاعلي باستخدام مكتبة D3.js يوضح كثافة الألعاب المكتشفة في كل إقليم حضاري
            </p>
          </div>
          <span style="background: rgba(200, 90, 50, 0.15); color: var(--mirath-terracotta-dark); font-size: 0.78rem; font-weight: 800; padding: 4px 10px; border-radius: 20px;">
            تفاعلي بنقر الفئات 🖱️
          </span>
        </div>

        <div id="home-d3-chart-mount" style="min-height: 280px; display: flex; justify-content: center; align-items: center;"></div>
      </section>

      <!-- Games Grid -->
      <div class="section-header">
        <h3 class="section-title">الألعاب المتاحة للعب الآن</h3>
      </div>
      <div class="games-grid">
        ${filteredGames.map(game => this.createGameCard(game)).join("")}
      </div>
    `;

    // Render interactive D3 chart
    this.renderHomeD3Chart();

    // Bind Home Events
    document.querySelectorAll(".filter-chip").forEach(btn => {
      btn.addEventListener("click", (e) => {
        this.selectedAudience = e.target.dataset.audience;
        this.playSound("click");
        this.renderHome();
      });
    });

    document.getElementById("btn-surprise-me").addEventListener("click", () => {
      const randomGame = MirathData.games[Math.floor(Math.random() * MirathData.games.length)];
      window.location.hash = `#game/${randomGame.slug}`;
      this.playSound("dice");
    });

    this.bindGameCardEvents();
  }

  renderHomeD3Chart() {
    const container = document.getElementById("home-d3-chart-mount");
    if (!container || typeof d3 === "undefined") return;

    container.innerHTML = "";

    const regionData = MirathData.regions.map(r => {
      const gamesInRegion = MirathData.games.filter(g => g.regionId === r.id);
      return {
        id: r.id,
        name: r.nameAr,
        count: gamesInRegion.length > 0 ? gamesInRegion.length : 1,
        realCount: gamesInRegion.length,
        games: gamesInRegion.map(g => g.titleAr)
      };
    });

    const colors = ["#C85A32", "#1F4E5B", "#C28B38", "#5B6A45", "#8B5A2B"];
    const width = 340;
    const height = 260;
    const radius = Math.min(width, height) / 2 - 15;
    const innerRadius = radius * 0.62;

    const wrapper = d3.select(container)
      .append("div")
      .style("display", "flex")
      .style("flex-direction", "column")
      .style("align-items", "center")
      .style("width", "100%");

    const svg = wrapper
      .append("svg")
      .attr("viewBox", `0 0 ${width} ${height}`)
      .attr("preserveAspectRatio", "xMidYMid meet")
      .style("max-width", "100%")
      .style("height", "auto")
      .append("g")
      .attr("transform", `translate(${width / 2}, ${height / 2})`);

    const colorScale = d3.scaleOrdinal()
      .domain(regionData.map(d => d.id))
      .range(colors);

    const pie = d3.pie()
      .value(d => d.count)
      .sort(null)
      .padAngle(0.04);

    const arc = d3.arc()
      .innerRadius(innerRadius)
      .outerRadius(radius)
      .cornerRadius(6);

    const hoverArc = d3.arc()
      .innerRadius(innerRadius - 4)
      .outerRadius(radius + 8)
      .cornerRadius(8);

    const centerTitle = svg.append("text")
      .attr("dy", "-14")
      .attr("text-anchor", "middle")
      .style("font-family", "Cairo, sans-serif")
      .style("font-size", "13px")
      .style("font-weight", "700")
      .style("fill", "#705E53")
      .text("إجمالي الألعاب");

    const centerValue = svg.append("text")
      .attr("dy", "14")
      .attr("text-anchor", "middle")
      .style("font-family", "Cairo, sans-serif")
      .style("font-size", "24px")
      .style("font-weight", "900")
      .style("fill", "#A8421E")
      .text(MirathData.games.length + " ألعاب");

    const centerSub = svg.append("text")
      .attr("dy", "32")
      .attr("text-anchor", "middle")
      .style("font-family", "Cairo, sans-serif")
      .style("font-size", "11px")
      .style("font-weight", "600")
      .style("fill", "#1F4E5B")
      .text("5 أقاليم جغرافية");

    const paths = svg.selectAll("path")
      .data(pie(regionData))
      .enter()
      .append("path")
      .attr("fill", d => colorScale(d.data.id))
      .attr("d", arc)
      .style("cursor", "pointer")
      .style("transition", "filter 0.2s ease")
      .on("mouseover", function(event, d) {
        d3.select(this)
          .transition()
          .duration(200)
          .attr("d", hoverArc)
          .style("filter", "brightness(1.15) drop-shadow(0 4px 8px rgba(0,0,0,0.2))");

        centerTitle.text(d.data.name);
        centerValue.text(d.data.realCount > 0 ? `${d.data.realCount} ألعاب` : "ألعاب قيد التوثيق");
        centerSub.text(d.data.games.length > 0 ? d.data.games.join("، ") : "ألعاب تكتيكية قديمة");
      })
      .on("mouseout", function() {
        d3.select(this)
          .transition()
          .duration(200)
          .attr("d", arc)
          .style("filter", "none");

        centerTitle.text("إجمالي الألعاب");
        centerValue.text(MirathData.games.length + " ألعاب");
        centerSub.text("5 أقاليم جغرافية");
      })
      .on("click", (event, d) => {
        window.location.hash = "#map";
      });

    paths.transition()
      .duration(900)
      .attrTween("d", function(d) {
        const i = d3.interpolate({ startAngle: 0, endAngle: 0 }, d);
        return function(t) { return arc(i(t)); };
      });

    const legend = wrapper.append("div")
      .style("display", "flex")
      .style("flex-wrap", "wrap")
      .style("justify-content", "center")
      .style("gap", "8px")
      .style("margin-top", "12px");

    regionData.forEach(d => {
      const item = legend.append("div")
        .style("display", "flex")
        .style("align-items", "center")
        .style("gap", "6px")
        .style("background", "var(--mirath-surface)")
        .style("border", "1px solid var(--mirath-card-border)")
        .style("padding", "4px 10px")
        .style("border-radius", "20px")
        .style("font-size", "11px")
        .style("font-weight", "700")
        .style("cursor", "pointer")
        .on("click", () => { window.location.hash = "#map"; });

      item.append("div")
        .style("width", "10px")
        .style("height", "10px")
        .style("border-radius", "50%")
        .style("background", colorScale(d.id));

      item.append("span")
        .text(`${d.name} (${d.realCount})`);
    });
  }

  createGameCard(game) {
    const country = MirathData.getCountryById(game.countryId);
    const isFav = this.favorites.includes(game.slug);

    return `
      <div class="game-card">
        <div class="game-card-header">
          <div class="game-title-group">
            <h3>${game.titleAr}</h3>
            <span class="en-title">${game.titleEn}</span>
          </div>
          <button class="btn-fav ${isFav ? 'active' : ''}" data-fav-slug="${game.slug}">
            ${isFav ? '❤️' : '🤍'}
          </button>
        </div>

        <div class="game-meta-tags">
          <span class="meta-badge culture">📍 ${country ? country.nameAr : game.culture}</span>
          <span class="meta-badge category">🏷️ ${game.primaryCategory}</span>
          <span class="meta-badge">⏳ ${game.playTime}</span>
          <span class="meta-badge">👥 ${game.minPlayers} لاعبين</span>
        </div>

        <p class="game-summary">${game.summaryAr}</p>

        ${this.isResearcherMode && game.claims.length > 0 ? `
          <div class="researcher-box">
            <strong>🔬 سند التحقيق:</strong> ${game.claims[0].textAr}
            <br><small style="color: var(--mirath-terracotta);">المصدر: ${game.claims[0].source}</small>
          </div>
        ` : ""}

        <div class="game-card-footer">
          <a href="#play/${game.slug}" class="btn-primary">🎮 العب الآن</a>
          <a href="#game/${game.slug}" class="btn-secondary">📖 التوثيق والقواعد</a>
        </div>
      </div>
    `;
  }

  bindGameCardEvents() {
    document.querySelectorAll(".btn-fav").forEach(btn => {
      btn.addEventListener("click", (e) => {
        const slug = e.currentTarget.dataset.favSlug;
        this.toggleFavorite(slug);
      });
    });
  }

  // ========================================================
  // VIEW: CATALOG
  // ========================================================
  renderCatalog() {
    this.pageTitle.textContent = "كتالوج الألعاب الموثقة";
    this.pageSubtitle.textContent = "ابحث بالاسم أو المنطقة أو النوع مع تصفية فورية";

    const normalizedQuery = this.normalizeArabic(this.searchQuery);
    const filtered = MirathData.games.filter(g => {
      if (!normalizedQuery) {
        return this.selectedCategory === "all" || g.primaryCategory === this.selectedCategory;
      }

      const country = MirathData.getCountryById(g.countryId);
      const region = MirathData.getRegionById(g.regionId);

      const searchableText = [
        g.titleAr,
        g.titleEn,
        g.originalTitle,
        ...(g.aliases || []),
        g.culture,
        g.primaryCategory,
        ...(g.tags || []),
        country ? country.nameAr : "",
        country ? country.nameEn : "",
        region ? region.nameAr : "",
        region ? region.nameEn : "",
        g.summaryAr
      ].join(" ");

      const matchQuery = this.normalizeArabic(searchableText).includes(normalizedQuery);
      const matchCat = this.selectedCategory === "all" || g.primaryCategory === this.selectedCategory;
      return matchQuery && matchCat;
    });

    const categories = ["all", ...new Set(MirathData.games.map(g => g.primaryCategory))];
    const quickTags = ["أور", "العراق", "القرق", "الأندلس", "أواري", "غانا", "شيسيما", "كينيا", "سباق", "مانكالا", "تطويق"];

    this.viewport.innerHTML = `
      <div style="margin-bottom: 18px; background: var(--mirath-cream); padding: 18px; border-radius: 16px; border: 1.5px solid var(--mirath-card-border);">
        <label style="display: block; font-weight: 800; font-size: 1.1rem; margin-bottom: 6px; color: var(--mirath-dark-brown);">
          🔍 البحث الفوري في الألعاب
        </label>
        <p style="font-size: 0.85rem; color: var(--mirath-muted-brown); margin-bottom: 12px;">
          ابحث بالاسم (أور، القرق...) أو المنطقة (العراق، كينيا...) أو النوع (سباق، مانكالا...)
        </p>
        <div style="position: relative;">
          <input
            type="text"
            id="catalog-search"
            class="form-control"
            placeholder="ابحث بالاسم أو المنطقة أو النوع..."
            value="${this.searchQuery}"
            style="font-size: 1.05rem; padding: 12px 18px 12px 42px; border-radius: 12px;"
          >
          ${this.searchQuery ? `
            <button id="btn-clear-search" style="position: absolute; left: 12px; top: 50%; transform: translateY(-50%); background: none; border: none; font-size: 1.2rem; cursor: pointer; color: var(--mirath-terracotta);">✕</button>
          ` : ''}
        </div>

        <div style="display: flex; gap: 6px; flex-wrap: wrap; align-items: center; margin-top: 10px;">
          <span style="font-size: 0.8rem; font-weight: 700; color: var(--mirath-muted-brown);">اقتراحات سريعة:</span>
          ${quickTags.map(tag => `
            <button class="filter-chip quick-search-chip ${this.searchQuery === tag ? 'active' : ''}" data-tag="${tag}" style="padding: 4px 10px; font-size: 0.78rem;">
              ${tag}
            </button>
          `).join("")}
        </div>

        ${this.searchQuery ? `
          <div style="margin-top: 10px; font-size: 0.85rem; color: var(--mirath-terracotta-dark); font-weight: 800;">
            ⚡ تصفية فورية للنتائج حسب: "${this.searchQuery}" (${filtered.length} لعبة)
          </div>
        ` : ''}
      </div>

      <div class="filter-scroll-bar">
        ${categories.map(cat => `
          <button class="filter-chip ${this.selectedCategory === cat ? 'active' : ''}" data-cat="${cat}">
            ${cat === 'all' ? '🌐 جميع التصنيفات' : cat}
          </button>
        `).join("")}
      </div>

      <div class="games-grid">
        ${filtered.length > 0
          ? filtered.map(g => this.createGameCard(g)).join("")
          : `<div style="grid-column: 1/-1; text-align: center; padding: 40px; background: var(--mirath-cream); border-radius: 16px;">
               <h3>لم يتم العثور على ألعاب مطابقة</h3>
               <p style="color: var(--mirath-muted-brown); margin: 8px 0 16px;">جرّب مسح كلمات البحث أو إظهار كل التصنيفات.</p>
               <button id="btn-reset-filters" class="btn-primary">إعادة تعيين البحث وإظهار كل الألعاب</button>
             </div>`
        }
      </div>
    `;

    const searchInput = document.getElementById("catalog-search");
    searchInput.focus();
    // Keep cursor at end
    const val = searchInput.value;
    searchInput.value = '';
    searchInput.value = val;

    searchInput.addEventListener("input", (e) => {
      this.searchQuery = e.target.value;
      this.renderCatalog();
    });

    const clearBtn = document.getElementById("btn-clear-search");
    if (clearBtn) {
      clearBtn.addEventListener("click", () => {
        this.searchQuery = "";
        this.renderCatalog();
      });
    }

    document.querySelectorAll(".quick-search-chip").forEach(btn => {
      btn.addEventListener("click", (e) => {
        const tag = e.target.dataset.tag;
        this.searchQuery = (this.searchQuery === tag) ? "" : tag;
        this.renderCatalog();
      });
    });

    const resetBtn = document.getElementById("btn-reset-filters");
    if (resetBtn) {
      resetBtn.addEventListener("click", () => {
        this.searchQuery = "";
        this.selectedCategory = "all";
        this.renderCatalog();
      });
    }

    document.querySelectorAll(".filter-chip:not(.quick-search-chip)").forEach(btn => {
      btn.addEventListener("click", (e) => {
        this.selectedCategory = e.target.dataset.cat;
        this.playSound("click");
        this.renderCatalog();
      });
    });

    this.bindGameCardEvents();
  }

  // ========================================================
  // VIEW: GAME DETAIL
  // ========================================================
  renderGameDetail(slug) {
    const game = MirathData.getGameBySlug(slug);
    if (!game) {
      this.viewport.innerHTML = `<h2>اللعبة غير موجودة</h2>`;
      return;
    }

    const country = MirathData.getCountryById(game.countryId);
    this.pageTitle.textContent = game.titleAr;
    this.pageSubtitle.textContent = game.titleEn;

    this.viewport.innerHTML = `
      <div class="detail-hero">
        <div class="detail-tags">
          <span class="meta-badge culture">📍 ${country ? country.nameAr : game.culture}</span>
          <span class="meta-badge category">🏛️ حقبة: ${game.era}</span>
          <span class="meta-badge">👥 ${game.minPlayers} لاعبين</span>
          <span class="meta-badge">⏱️ ${game.playTime}</span>
        </div>
        <h2 class="detail-title">${game.titleAr} (${game.titleEn})</h2>
        <p class="detail-subtitle"><strong>الاسم التاريخي:</strong> ${game.originalTitle} | <strong>المسميات البديلة:</strong> ${game.aliases.join("، ")}</p>
        <p style="font-size: 1.05rem; line-height: 1.7; margin-bottom: 20px;">${game.summaryAr}</p>

        <div style="display: flex; gap: 12px; flex-wrap: wrap;">
          <a href="#play/${game.slug}" class="btn-primary">🎮 العب الآن على المتصفح</a>
          <button id="btn-print-board-spec" class="btn-secondary">🖨️ طباعة لوحة اللعب الورقية (A4)</button>
          <a href="#suggest" class="btn-secondary">✍️ اقترح تصحيحاً للقواعد</a>
        </div>
      </div>

      <!-- Rules Breakdown -->
      <div class="doc-section">
        <h3 class="doc-section-title">📜 خطوات وقواعد اللعب الأصلية</h3>
        <p style="margin-bottom: 16px;"><strong>شرط النصر:</strong> ${game.winCondition}</p>
        <div class="rules-step-list">
          ${game.steps.map(s => `
            <div class="rule-step-card">
              <h4>الخطوة ${s.num}: ${s.title}</h4>
              <p style="margin-bottom: 6px;">${s.detail}</p>
              <small style="color: var(--mirath-terracotta-dark);">💡 <strong>نصيحة إستراتيجية:</strong> ${s.tip}</small>
            </div>
          `).join("")}
        </div>
      </div>

      <!-- Archaeological Claims & Sources -->
      <div class="doc-section">
        <h3 class="doc-section-title">🔬 التوثيق الأكاديمي والأدلة الأثرية</h3>
        <p style="font-size: 0.9rem; color: var(--mirath-muted-brown); margin-bottom: 14px;">
          تلتزم منصة مِرث بصرامة التوثيق التاريخي دون أي اختلاق لقواعد أو تواريخ غير مثبتة.
        </p>
        ${game.claims.map(c => `
          <div class="claim-card">
            <strong>الدليل المحقق:</strong> ${c.textAr}
            <div style="font-size: 0.85rem; color: var(--mirath-lapis); margin-top: 4px;">المصدر: ${c.source}</div>
          </div>
        `).join("")}

        <h4 style="margin: 18px 0 10px; font-weight: 800;">المراجع والمنشورات العلمية:</h4>
        ${game.sources.map(src => `
          <div class="source-item">
            📚 <strong>${src.label}</strong> — تأليف: ${src.author} (${src.year})، ${src.publisher}
          </div>
        `).join("")}
      </div>

      <!-- Printable Board Specification -->
      <div class="doc-section">
        <h3 class="doc-section-title">🖨️ مواصفات اللوحة القابلة للطباعة</h3>
        <p><strong>العنوان:</strong> ${game.printableBoard.title}</p>
        <p><strong>الأبعاد:</strong> ${game.printableBoard.dimensions}</p>
        <p><strong>إرشادات التنفيذ المنزلي/المدرسي:</strong> ${game.printableBoard.instructions}</p>
      </div>
    `;

    document.getElementById("btn-print-board-spec").addEventListener("click", () => {
      window.print();
    });
  }

  // ========================================================
  // VIEW: PLAY GAME (Interactive Boards)
  // ========================================================
  renderPlayGame(slug) {
    const game = MirathData.getGameBySlug(slug);
    if (!game) {
      this.viewport.innerHTML = `<h2>اللعبة غير موجودة</h2>`;
      return;
    }

    this.pageTitle.textContent = `تجربة اللعب: ${game.titleAr}`;
    this.pageSubtitle.textContent = `محرك لعب تفاعلي بالمتصفح (${game.titleEn})`;

    this.viewport.innerHTML = `
      <div class="game-play-container">
        <div class="play-header">
          <div>
            <h2 style="font-size: 1.5rem; color: var(--mirath-terracotta-dark);">${game.titleAr}</h2>
            <span style="font-size: 0.85rem; color: var(--mirath-muted-brown);">قواعد موثقة خالية من التزييف</span>
          </div>

          <div class="play-modes">
            <button class="play-mode-btn ${this.activePlayMode === 'local' ? 'active' : ''}" data-mode="local">
              👥 لعب محلي (لاعبان)
            </button>
            <button class="play-mode-btn ${this.activePlayMode === 'ai' ? 'active' : ''}" data-mode="ai">
              🤖 ضد الذكاء الاصطناعي
            </button>
            <button id="btn-restart-game" class="icon-btn" title="إعادة تشغيل اللعبة">🔄</button>
          </div>
        </div>

        <div id="play-status-bar" class="play-status-bar">
          <!-- Dynamic Engine Status Message -->
        </div>

        <div id="active-board-surface" style="display: flex; justify-content: center; margin: 24px 0;">
          <!-- Board rendered based on engine -->
        </div>

        <div style="display: flex; justify-content: space-between; align-items: center; margin-top: 24px; padding-top: 16px; border-top: 1px solid var(--mirath-card-border); flex-wrap: wrap; gap: 10px;">
          <a href="#game/${game.slug}" class="btn-secondary">📖 مراجعة القواعد والتاريخ</a>
          <button id="btn-trigger-restart" class="btn-secondary">🔄 جولة جديدة</button>
        </div>
      </div>
    `;

    // Bind mode buttons
    document.querySelectorAll(".play-mode-btn").forEach(btn => {
      btn.addEventListener("click", (e) => {
        this.activePlayMode = e.target.dataset.mode;
        this.playSound("click");
        this.renderPlayGame(slug);
      });
    });

    document.getElementById("btn-restart-game").addEventListener("click", () => this.restartGame(slug));
    document.getElementById("btn-trigger-restart").addEventListener("click", () => this.restartGame(slug));

    this.renderActiveEngineBoard(slug);
  }

  restartGame(slug) {
    if (slug === "royal-game-of-ur") this.urEngine.reset();
    else if (slug === "alquerque") this.alquerqueEngine.reset();
    else if (slug === "oware") this.owareEngine.reset();
    else if (slug === "shisima") this.shisimaEngine.reset();

    this.playSound("click");
    this.renderPlayGame(slug);
  }

  renderActiveEngineBoard(slug) {
    const statusElem = document.getElementById("play-status-bar");
    const surfaceElem = document.getElementById("active-board-surface");

    if (slug === "royal-game-of-ur") {
      this.renderUrBoard(statusElem, surfaceElem);
    } else if (slug === "alquerque") {
      this.renderAlquerqueBoard(statusElem, surfaceElem);
    } else if (slug === "oware") {
      this.renderOwareBoard(statusElem, surfaceElem);
    } else if (slug === "shisima") {
      this.renderShisimaBoard(statusElem, surfaceElem);
    }
  }

  // 1. Ur Board Render & Handling
  renderUrBoard(statusElem, surfaceElem) {
    statusElem.textContent = this.urEngine.statusMessage;

    // Build Ur 3x8 layout cells
    // Ur track is: P1 path (4,3,2,1 -> 5..12 -> 14,13), P2 path (symmetric)
    const p1Off = this.urEngine.p1.filter(p => p > 14).length;
    const p2Off = this.urEngine.p2.filter(p => p > 14).length;

    let cellsHtml = "";
    // 3 rows, 8 cols
    for (let r = 0; r < 3; r++) {
      for (let c = 0; c < 8; c++) {
        // H-shape blank gaps at (r=0, c=4,5) and (r=2, c=4,5)
        const isGap = (r === 0 || r === 2) && (c === 4 || c === 5);
        const isRosette = (r === 0 && (c === 0 || c === 6)) || (r === 2 && (c === 0 || c === 6)) || (r === 1 && c === 3);

        if (isGap) {
          cellsHtml += `<div class="ur-cell empty-gap"></div>`;
        } else {
          cellsHtml += `
            <div class="ur-cell ${isRosette ? 'rosette' : ''}" data-row="${r}" data-col="${c}">
            </div>
          `;
        }
      }
    }

    surfaceElem.innerHTML = `
      <div class="ur-board-wrapper">
        <!-- Pieces Track Status -->
        <div style="display: flex; justify-content: space-between; width: 100%; max-width: 500px; font-weight: 800;">
          <div style="color: var(--mirath-terracotta);">
            🔴 قطع اللاعب 1 المنهية: ${p1Off} / 4
          </div>
          <div style="color: var(--mirath-gold);">
            🟡 قطع اللاعب 2 المنهية: ${p2Off} / 4
          </div>
        </div>

        <!-- 3x8 Board -->
        <div class="ur-board">
          ${cellsHtml}
        </div>

        <!-- 4 Tetrahedral Dice Rolling Tray -->
        <div class="dice-tray">
          <div class="dice-list">
            ${this.urEngine.diceDetails.map(d => `
              <div class="die-tetra ${d === 1 ? 'white-tip' : ''}">
                ${d === 1 ? '▲' : '△'}
              </div>
            `).join("")}
          </div>
          <button id="btn-roll-ur-dice" class="btn-primary" ${this.urEngine.currentRoll !== null || this.urEngine.winner !== null ? 'disabled' : ''}>
            🎲 ارمِ النرد
          </button>
        </div>

        <!-- Movable Pieces Buttons -->
        <div style="display: flex; gap: 8px; flex-wrap: wrap;">
          <span style="font-weight: 700; align-self: center;">قطع اللاعب ${this.urEngine.currentTurn}:</span>
          ${this.urEngine.getLegalMoves().map(idx => `
            <button class="btn-secondary btn-move-piece" data-idx="${idx}">
              تحريك القطعة رقم ${idx + 1}
            </button>
          `).join("")}
        </div>
      </div>
    `;

    document.getElementById("btn-roll-ur-dice").addEventListener("click", () => {
      this.playSound("dice");
      this.urEngine.rollDice();
      this.renderUrBoard(statusElem, surfaceElem);

      if (this.activePlayMode === "ai" && this.urEngine.currentTurn === 2 && this.urEngine.winner === null) {
        setTimeout(() => {
          this.urEngine.makeAiMove();
          this.renderUrBoard(statusElem, surfaceElem);
        }, 800);
      }
    });

    document.querySelectorAll(".btn-move-piece").forEach(btn => {
      btn.addEventListener("click", (e) => {
        const pieceIdx = parseInt(e.target.dataset.idx, 10);
        this.playSound("click");
        this.urEngine.movePiece(pieceIdx);
        this.renderUrBoard(statusElem, surfaceElem);

        if (this.urEngine.winner) this.playSound("win");

        if (this.activePlayMode === "ai" && this.urEngine.currentTurn === 2 && this.urEngine.winner === null) {
          setTimeout(() => {
            this.urEngine.makeAiMove();
            this.renderUrBoard(statusElem, surfaceElem);
            if (this.urEngine.winner) this.playSound("win");
          }, 800);
        }
      });
    });
  }

  // 2. Alquerque Board Render & Handling
  renderAlquerqueBoard(statusElem, surfaceElem) {
    statusElem.textContent = this.alquerqueEngine.statusMessage;

    // SVG Lines for 5x5 grid (horizontal, vertical, diagonal)
    let pointsHtml = "";
    for (let r = 0; r < 5; r++) {
      for (let c = 0; c < 5; c++) {
        const idx = r * 5 + c;
        const piece = this.alquerqueEngine.board[idx];
        const isSelected = this.alquerqueEngine.selected === idx;

        // Position percentage: 10% + col * 20%
        const leftPercent = 10 + c * 20;
        const topPercent = 10 + r * 20;

        pointsHtml += `
          <div class="alquerque-point" data-idx="${idx}" style="left: ${leftPercent}%; top: ${topPercent}%;">
            ${piece === 1 ? `<div class="alquerque-piece p1 ${isSelected ? 'selected' : ''}"></div>` : ''}
            ${piece === 2 ? `<div class="alquerque-piece p2 ${isSelected ? 'selected' : ''}"></div>` : ''}
            ${piece === 0 ? `<div style="width: 10px; height: 10px; border-radius: 50%; background: #A2703F;"></div>` : ''}
          </div>
        `;
      }
    }

    surfaceElem.innerHTML = `
      <div class="alquerque-container">
        <div class="alquerque-board">
          <svg class="alquerque-grid-svg" viewBox="0 0 100 100">
            <!-- Horizontals -->
            <line x1="10" y1="10" x2="90" y2="10" stroke="#734522" stroke-width="1.5" />
            <line x1="10" y1="30" x2="90" y2="30" stroke="#734522" stroke-width="1.5" />
            <line x1="10" y1="50" x2="90" y2="50" stroke="#734522" stroke-width="1.5" />
            <line x1="10" y1="70" x2="90" y2="70" stroke="#734522" stroke-width="1.5" />
            <line x1="10" y1="90" x2="90" y2="90" stroke="#734522" stroke-width="1.5" />

            <!-- Verticals -->
            <line x1="10" y1="10" x2="10" y2="90" stroke="#734522" stroke-width="1.5" />
            <line x1="30" y1="10" x2="30" y2="90" stroke="#734522" stroke-width="1.5" />
            <line x1="50" y1="10" x2="50" y2="90" stroke="#734522" stroke-width="1.5" />
            <line x1="70" y1="10" x2="70" y2="90" stroke="#734522" stroke-width="1.5" />
            <line x1="90" y1="10" x2="90" y2="90" stroke="#734522" stroke-width="1.5" />

            <!-- Diagonals -->
            <line x1="10" y1="10" x2="90" y2="90" stroke="#734522" stroke-width="1.5" />
            <line x1="10" y1="90" x2="90" y2="10" stroke="#734522" stroke-width="1.5" />
            <line x1="10" y1="50" x2="50" y2="10" stroke="#734522" stroke-width="1.5" />
            <line x1="50" y1="10" x2="90" y2="50" stroke="#734522" stroke-width="1.5" />
            <line x1="90" y1="50" x2="50" y2="90" stroke="#734522" stroke-width="1.5" />
            <line x1="50" y1="90" x2="10" y2="50" stroke="#734522" stroke-width="1.5" />
          </svg>
          ${pointsHtml}
        </div>
      </div>
    `;

    document.querySelectorAll(".alquerque-point").forEach(pt => {
      pt.addEventListener("click", (e) => {
        const idx = parseInt(e.currentTarget.dataset.idx, 10);
        const actionDone = this.alquerqueEngine.clickPoint(idx);

        if (actionDone) {
          this.playSound("click");
          this.renderAlquerqueBoard(statusElem, surfaceElem);
          if (this.alquerqueEngine.winner) this.playSound("win");

          if (this.activePlayMode === "ai" && this.alquerqueEngine.currentTurn === 2 && !this.alquerqueEngine.winner) {
            setTimeout(() => {
              this.alquerqueEngine.makeAiMove();
              this.renderAlquerqueBoard(statusElem, surfaceElem);
              if (this.alquerqueEngine.winner) this.playSound("win");
            }, 600);
          }
        }
      });
    });
  }

  // 3. Oware Board Render & Handling
  renderOwareBoard(statusElem, surfaceElem) {
    statusElem.textContent = this.owareEngine.statusMessage;

    // P1 pits (0..5) and P2 pits (12..7 descending for counter-clockwise loop)
    const p1Pits = [0, 1, 2, 3, 4, 5];
    const p2Pits = [12, 11, 10, 9, 8, 7];

    surfaceElem.innerHTML = `
      <div class="oware-container">
        <div class="oware-board">
          <!-- P2 Store (left) -->
          <div class="mancala-store">
            <span style="font-size: 0.8rem; opacity: 0.8;">مخزن 2</span>
            <span style="font-size: 1.6rem; color: #FFD166;">${this.owareEngine.pits[13]}</span>
          </div>

          <!-- Pits Grid -->
          <div class="mancala-pits-grid">
            <!-- Row 2: Player 2 pits -->
            <div class="mancala-row">
              ${p2Pits.map(idx => `
                <div class="mancala-pit ${this.owareEngine.currentTurn !== 2 ? 'disabled' : ''}" data-pit="${idx}">
                  <span class="seed-counter">${this.owareEngine.pits[idx]}</span>
                </div>
              `).join("")}
            </div>

            <!-- Row 1: Player 1 pits -->
            <div class="mancala-row">
              ${p1Pits.map(idx => `
                <div class="mancala-pit ${this.owareEngine.currentTurn !== 1 ? 'disabled' : ''}" data-pit="${idx}">
                  <span class="seed-counter">${this.owareEngine.pits[idx]}</span>
                </div>
              `).join("")}
            </div>
          </div>

          <!-- P1 Store (right) -->
          <div class="mancala-store">
            <span style="font-size: 0.8rem; opacity: 0.8;">مخزن 1</span>
            <span style="font-size: 1.6rem; color: #FFD166;">${this.owareEngine.pits[6]}</span>
          </div>
        </div>
      </div>
    `;

    document.querySelectorAll(".mancala-pit").forEach(pit => {
      pit.addEventListener("click", (e) => {
        const idx = parseInt(e.currentTarget.dataset.pit, 10);
        const success = this.owareEngine.clickPit(idx);
        if (success) {
          this.playSound("click");
          this.renderOwareBoard(statusElem, surfaceElem);
          if (this.owareEngine.winner) this.playSound("win");

          if (this.activePlayMode === "ai" && this.owareEngine.currentTurn === 2 && !this.owareEngine.winner) {
            setTimeout(() => {
              this.owareEngine.makeAiMove();
              this.renderOwareBoard(statusElem, surfaceElem);
              if (this.owareEngine.winner) this.playSound("win");
            }, 700);
          }
        }
      });
    });
  }

  // 4. Shisima Board Render & Handling
  renderShisimaBoard(statusElem, surfaceElem) {
    statusElem.textContent = this.shisimaEngine.statusMessage;

    // 8 perimeter points around circle, plus center point
    const radius = 40;
    const center = 50;
    let pointsHtml = "";

    for (let i = 0; i < 8; i++) {
      const angle = (i * 45 - 90) * (Math.PI / 180);
      const x = center + radius * Math.cos(angle);
      const y = center + radius * Math.sin(angle);
      const piece = this.shisimaEngine.board[i];
      const isSelected = this.shisimaEngine.selected === i;

      pointsHtml += `
        <div class="shisima-point" data-idx="${i}" style="left: ${x}%; top: ${y}%;">
          ${piece === 1 ? `<div class="shisima-piece p1 ${isSelected ? 'selected' : ''}"></div>` : ''}
          ${piece === 2 ? `<div class="shisima-piece p2 ${isSelected ? 'selected' : ''}"></div>` : ''}
          ${piece === 0 ? `<div style="width: 12px; height: 12px; border-radius: 50%; background: #8F5425;"></div>` : ''}
        </div>
      `;
    }

    // Center point (8)
    const centerPiece = this.shisimaEngine.board[8];
    const isCenterSelected = this.shisimaEngine.selected === 8;
    pointsHtml += `
      <div class="shisima-point center" data-idx="8" style="left: 50%; top: 50%;">
        ${centerPiece === 1 ? `<div class="shisima-piece p1 ${isCenterSelected ? 'selected' : ''}"></div>` : ''}
        ${centerPiece === 2 ? `<div class="shisima-piece p2 ${isCenterSelected ? 'selected' : ''}"></div>` : ''}
        ${centerPiece === 0 ? `<div style="font-size: 0.8rem; color: var(--mirath-lapis); font-weight: 800;">بئر</div>` : ''}
      </div>
    `;

    surfaceElem.innerHTML = `
      <div class="shisima-container">
        <div class="shisima-board">
          <svg class="shisima-grid-svg" viewBox="0 0 100 100">
            <!-- Perimeter Octagon -->
            <polygon points="50,10 78,22 90,50 78,78 50,90 22,78 10,50 22,22" fill="none" stroke="#734522" stroke-width="2" />
            <!-- Diameters through center -->
            <line x1="50" y1="10" x2="50" y2="90" stroke="#734522" stroke-width="1.5" />
            <line x1="10" y1="50" x2="90" y2="50" stroke="#734522" stroke-width="1.5" />
            <line x1="22" y1="22" x2="78" y2="78" stroke="#734522" stroke-width="1.5" />
            <line x1="78" y1="22" x2="22" y2="78" stroke="#734522" stroke-width="1.5" />
          </svg>
          ${pointsHtml}
        </div>
      </div>
    `;

    document.querySelectorAll(".shisima-point").forEach(pt => {
      pt.addEventListener("click", (e) => {
        const idx = parseInt(e.currentTarget.dataset.idx, 10);
        const changed = this.shisimaEngine.clickPoint(idx);
        if (changed) {
          this.playSound("click");
          this.renderShisimaBoard(statusElem, surfaceElem);
          if (this.shisimaEngine.winner) this.playSound("win");

          if (this.activePlayMode === "ai" && this.shisimaEngine.currentTurn === 2 && !this.shisimaEngine.winner) {
            setTimeout(() => {
              this.shisimaEngine.makeAiMove();
              this.renderShisimaBoard(statusElem, surfaceElem);
              if (this.shisimaEngine.winner) this.playSound("win");
            }, 600);
          }
        }
      });
    });
  }

  // ========================================================
  // VIEW: MAP & REGIONS
  // ========================================================
  renderMap() {
    this.pageTitle.textContent = "خريطة الأقاليم والحضارات القديمة";
    this.pageSubtitle.textContent = "استكشف مهد الألعاب التقليدية وجغرافيتها التراثية";

    this.viewport.innerHTML = `
      <div style="margin-bottom: 24px;">
        <p style="font-size: 1.05rem; color: var(--mirath-muted-brown);">
          توزعت ألعاب الألواح عبر ممرات التجارة وطرق الحرير والتقاليد الشفهية. انقر على أي دولة أو إقليم لاستعراض ثقافته وألعابه المحققة.
        </p>
      </div>

      <div style="display: flex; flex-direction: column; gap: 20px;">
        ${MirathData.regions.map(r => {
          const regionCountries = MirathData.countries.filter(c => c.regionId === r.id);
          return `
            <div class="doc-section">
              <div style="display: flex; align-items: center; gap: 8px; margin-bottom: 6px;">
                <span style="font-size: 1.4rem;">🌍</span>
                <h3 style="font-size: 1.3rem; color: var(--mirath-terracotta-dark); font-weight: 800;">${r.nameAr}</h3>
                <span style="font-size: 0.85rem; color: var(--mirath-muted-brown); font-family: system-ui;">(${r.nameEn})</span>
              </div>
              <p style="color: var(--mirath-dark-brown); margin-bottom: 14px;">${r.summaryAr}</p>

              <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(260px, 1fr)); gap: 12px;">
                ${regionCountries.map(c => {
                  const countryGames = MirathData.games.filter(g => g.countryId === c.id);
                  return `
                    <div style="background: var(--mirath-surface); border-radius: 10px; padding: 14px; border: 1px solid var(--mirath-card-border);">
                      <h4 style="font-weight: 800; color: var(--mirath-dark-brown); margin-bottom: 4px;">📍 ${c.nameAr}</h4>
                      <p style="font-size: 0.85rem; color: var(--mirath-muted-brown); margin-bottom: 8px;">${c.summaryAr}</p>
                      <div style="font-size: 0.82rem; font-weight: 700; color: var(--mirath-terracotta);">
                        الألعاب الموثقة: ${countryGames.map(cg => `<a href="#game/${cg.slug}" style="color: var(--mirath-terracotta); text-decoration: underline;">${cg.titleAr}</a>`).join("، ") || "قيد التحقيق"}
                      </div>
                    </div>
                  `;
                }).join("")}
              </div>
            </div>
          `;
        }).join("")}
      </div>
    `;
  }

  // ========================================================
  // VIEW: GLOSSARY
  // ========================================================
  renderGlossary() {
    this.pageTitle.textContent = "معجم المصطلحات اللدولوجية والتاريخية";
    this.pageSubtitle.textContent = "المفاهيم التأسيسية لألعاب الألواح العالمية";

    this.viewport.innerHTML = `
      <div style="display: flex; flex-direction: column; gap: 16px;">
        ${MirathData.glossary.map(item => `
          <div class="doc-section">
            <h3 style="font-size: 1.25rem; color: var(--mirath-terracotta-dark); font-weight: 800; margin-bottom: 4px;">
              📖 ${item.termAr}
            </h3>
            <span style="font-size: 0.82rem; color: var(--mirath-lapis); font-weight: 700;">الأصل اللغوي: ${item.originLanguageAr}</span>
            <p style="margin: 8px 0 12px; font-size: 0.98rem; line-height: 1.6;">${item.definitionAr}</p>
            <div style="font-size: 0.85rem;">
              <strong>الألعاب المرتبطة:</strong>
              ${item.relatedGamesSlugs.map(slug => {
                const g = MirathData.getGameBySlug(slug);
                return g ? `<a href="#game/${slug}" style="color: var(--mirath-terracotta); font-weight: 700; margin-right: 6px;">${g.titleAr}</a>` : slug;
              }).join("، ")}
            </div>
          </div>
        `).join("")}
      </div>
    `;
  }

  // ========================================================
  // VIEW: SUGGEST GAME
  // ========================================================
  renderSuggest() {
    this.pageTitle.textContent = "اقترح لعبة للتحقيق والتوثيق";
    this.pageSubtitle.textContent = "شاركنا تراث منطقتك وألعاب أجدادك المنسية";

    this.viewport.innerHTML = `
      <div class="form-container">
        <h2 style="font-size: 1.4rem; color: var(--mirath-terracotta-dark); margin-bottom: 8px;">استمارة اقتراح لعبة شعبية</h2>
        <p style="font-size: 0.9rem; color: var(--mirath-muted-brown); margin-bottom: 20px;">
          تدخل كل مساهمة طابور التدقيق والتحقق الأكاديمي لمطابقتها مع المخطوطات والوثائق الشفهية والمتحفية.
        </p>

        <form id="suggest-form">
          <div class="form-group">
            <label class="form-label">اسم اللعبة الأصلي أو المتداول *</label>
            <input type="text" id="sug-title" class="form-control" required placeholder="مثال: الخربقة، طاب، الدريسة...">
          </div>

          <div class="form-group">
            <label class="form-label">الدولة أو المنطقة الثقافية *</label>
            <input type="text" id="sug-country" class="form-control" required placeholder="مثال: تونس، السودان، نجد، الشام...">
          </div>

          <div class="form-group">
            <label class="form-label">وصف اللعبة، أدواتها، وكيف تُلعب؟ *</label>
            <textarea id="sug-desc" class="form-control" required placeholder="اذكر عدد القطع، شكل الرقعة، هل تلعب بحصى أو نرد أو عظام، وقواعد الفوز..."></textarea>
          </div>

          <div class="form-group">
            <label class="form-label">مصدر موثوق أو مرجع تاريخي/كتاب (اختياري)</label>
            <input type="text" id="sug-source" class="form-control" placeholder="كتاب، دراسة فلكلورية، أو شاهد عيان من كبار السن...">
          </div>

          <div class="form-group">
            <label class="form-label">اسمك لإضافته في قائمة الشكر والاعتمادات</label>
            <input type="text" id="sug-author" class="form-control" placeholder="اسمك الكريم أو لقبك البحثي...">
          </div>

          <button type="submit" class="btn-primary" style="width: 100%; justify-content: center;">
            ✉️ إرسال الاقتراح للمراجعة
          </button>
        </form>
      </div>
    `;

    document.getElementById("suggest-form").addEventListener("submit", (e) => {
      e.preventDefault();
      const title = document.getElementById("sug-title").value;
      const country = document.getElementById("sug-country").value;
      const desc = document.getElementById("sug-desc").value;
      const source = document.getElementById("sug-source").value;
      const author = document.getElementById("sug-author").value;

      const suggestions = JSON.parse(localStorage.getItem("mirath_suggestions") || "[]");
      suggestions.push({ title, country, desc, source, author, date: new Date().toISOString() });
      localStorage.setItem("mirath_suggestions", JSON.stringify(suggestions));

      this.playSound("win");
      this.showSnackbar("🎉 تم حفظ اقتراحك بنجاح وسيدخل طابور التحقيق التاريخي!");
      setTimeout(() => { window.location.hash = "#home"; }, 1500);
    });
  }

  // ========================================================
  // VIEW: ADMIN & CONTRIBUTIONS
  // ========================================================
  renderAdmin() {
    this.pageTitle.textContent = "بوابة الإدارة والأرشفة";
    this.pageSubtitle.textContent = "إدارة البيانات والمساهمات والمخرجات";

    const suggestions = JSON.parse(localStorage.getItem("mirath_suggestions") || "[]");

    this.viewport.innerHTML = `
      <div style="display: flex; flex-direction: column; gap: 20px;">
        <div class="doc-section" style="border: 1px solid var(--mirath-sandstone);">
          <h3 class="doc-section-title">🎨 إعدادات العرض ومظهر المنصة</h3>
          <p style="margin-bottom: 12px; color: var(--mirath-muted-brown);">
            يمكنك تفعيل سمة ألوان المخطوطات القديمة الداكنة لتخفيف إجهاد العين أثناء المطالعة المطولة والبحث التراثي.
          </p>
          <label style="display: flex; align-items: center; justify-content: space-between; background: var(--mirath-surface); padding: 12px 16px; border-radius: 10px; cursor: pointer; border: 1px solid var(--mirath-card-border);">
            <div>
              <strong>الوضع الليلي (ألوان المخطوطات القديمة)</strong>
              <div style="font-size: 0.82rem; color: var(--mirath-muted-brown);">حبر داكن، رق عتيق، وخطوط مذهبة مريحة للقراءة</div>
            </div>
            <input type="checkbox" id="admin-dark-mode-switch" ${this.isDarkMode ? "checked" : ""} style="width: 22px; height: 22px; accent-color: var(--mirath-gold); cursor: pointer;">
          </label>
        </div>

        <div class="doc-section">
          <h3 class="doc-section-title">📊 إحصائيات منصة مِرث الرقمية</h3>
          <p>عدد الألعاب الموثقة: <strong>${MirathData.games.length} ألعاب</strong></p>
          <p>عدد الأقاليم المسجلة: <strong>${MirathData.regions.length} أقاليم</strong></p>
          <p>عدد المصطلحات بالمعجم: <strong>${MirathData.glossary.length} مصطلحاً</strong></p>
          <p>المساهمات والاقتراحات المسجلة محلياً: <strong>${suggestions.length} اقتراح</strong></p>
        </div>

        <div class="doc-section">
          <h3 class="doc-section-title">📥 تصدير وتكامل البيانات</h3>
          <p style="margin-bottom: 14px;">يمكنك تصدير قاعدة بيانات الألعاب الكاملة بصيغة JSON متوافقة لأي تطبيق أو بحث أكاديمي.</p>
          <button id="btn-admin-export" class="btn-primary">📥 تصدير ملف mirath-catalog.json</button>
        </div>

        <div class="doc-section">
          <h3 class="doc-section-title">📝 قائمة المقترحات المدخلة (${suggestions.length})</h3>
          ${suggestions.length === 0 ? `<p style="color: var(--mirath-muted-brown);">لا توجد مقترحات مسجلة حتى الآن.</p>` : `
            <div style="display: flex; flex-direction: column; gap: 10px;">
              ${suggestions.map(s => `
                <div style="background: var(--mirath-surface); border-radius: 8px; padding: 12px; border: 1px solid var(--mirath-card-border);">
                  <strong>${s.title}</strong> (${s.country}) — بواسطة: ${s.author || 'فاعل خير'}
                  <p style="font-size: 0.85rem; margin-top: 4px;">${s.desc}</p>
                  ${s.source ? `<small style="color: var(--mirath-lapis);">المصدر: ${s.source}</small>` : ''}
                </div>
              `).join("")}
            </div>
          `}
        </div>
      </div>
    `;

    document.getElementById("btn-admin-export").addEventListener("click", () => this.exportJsonCatalog());
    const adminThemeSwitch = document.getElementById("admin-dark-mode-switch");
    if (adminThemeSwitch) {
      adminThemeSwitch.addEventListener("change", (e) => this.toggleTheme(e.target.checked));
    }
  }

  exportJsonCatalog() {
    const exportData = {
      platform: "Mirath - Forgotten Games of the World",
      version: "1.0.0",
      exportedAt: new Date().toISOString(),
      gamesCount: MirathData.games.length,
      games: MirathData.games,
      regions: MirathData.regions,
      countries: MirathData.countries,
      glossary: MirathData.glossary
    };

    const blob = new Blob([JSON.stringify(exportData, null, 2)], { type: "application/json" });
    const url = URL.createObjectURL(blob);
    const a = document.createElement("a");
    a.href = url;
    a.download = `mirath-catalog-${new Date().toISOString().split("T")[0]}.json`;
    a.click();
    URL.revokeObjectURL(url);
    this.showSnackbar("📥 تم تنزيل كتالوج مِرث بصيغة JSON");
  }
}

// Initialize on DOM ready
document.addEventListener("DOMContentLoaded", () => {
  window.mirathApp = new MirathApp();
});
