import { G as wl, a as yl, S as Su, J as Fc, s as se$1, y, K as uu, _ as _t$1, L as ou, E as Eo, V as Ve, P as Uc, W as Wc, g as g$1, Q as fu, q as ql, I as Io, U as Ue$1, i as iu, O as O$1, b as Vu, A as hn, B as le, v as ve$1, Z as su, $ as lu, a0 as xu, w as fe, a1 as gu, a2 as J, a3 as Pc, a4 as au, a5 as Ct$1, a6 as ht$1, R as Ru, a7 as Oo, c as Iu, j as ju, a8 as rl, a9 as Pu, aa as Tc, ab as $u, Y as Yl, ac as Ic, h as hu, ad as yo, ae as bo, af as mu, ag as mo, ah as Fl, ai as _o, aj as Cu } from "./indexhtml-X9N9p0Tk.js";
import { g, h } from "./state-CBOKMTMT-DiIKswy3.js";
import { h as h$1, u } from "./overlay-monkeypatch-CJ80DYS6-BLRDpSId.js";
import { o } from "./icons-Bm90QnWr-IWQY1ovD.js";
import { e } from "./early-project-state-CqEloDes-CqEloDes.js";
const dt = 1, me = 36, ct = 18;
function ht(e2, t) {
  if (e2.length === 0)
    return;
  const i = pt(e2, t);
  for (const n in e2)
    e2[n].style.setProperty("--content-height", `${i[n]}px`);
}
function pt(e2, t) {
  const i = e2.length, n = e2.filter((s) => s.panelInfo && s.panelInfo.expanded).length, o2 = i - n;
  return e2.map((s) => {
    const a = s.panelInfo;
    return a && !a.expanded ? me : (t.offsetHeight - (t.position === "bottom" ? ct : 0) - o2 * me - i * dt) / n;
  });
}
var ut = Object.defineProperty, gt = Object.getOwnPropertyDescriptor, _ = (e2, t, i, n) => {
  for (var o2 = n > 1 ? void 0 : n ? gt(t, i) : t, s = e2.length - 1, a; s >= 0; s--)
    (a = e2[s]) && (o2 = (n ? a(t, i, o2) : a(o2)) || o2);
  return n && o2 && ut(t, i, o2), o2;
};
const K = "data-drag-initial-index", j = "data-drag-final-index";
let k = class extends ql {
  constructor() {
    super(...arguments), this.position = "right", this.opened = false, this.keepOpen = false, this.resizing = false, this.closingForcefully = false, this.draggingSectionPanel = null, this.documentMouseUpListener = () => {
      this.resizing && y.emit("user-select", { allowSelection: true }), this.resizing = false, g$1.setDrawerResizing(false), this.removeAttribute("resizing");
    }, this.activationAnimationTransitionEndListener = () => {
      this.style.removeProperty("--closing-delay"), this.style.removeProperty("--initial-position"), this.removeEventListener("transitionend", this.activationAnimationTransitionEndListener);
    }, this.resizingMouseMoveListener = (e2) => {
      if (!this.resizing)
        return;
      const { x: t, y: i } = e2;
      e2.stopPropagation(), e2.preventDefault(), requestAnimationFrame(() => {
        let n;
        if (this.position === "right") {
          const o2 = document.body.clientWidth - t;
          this.style.setProperty("--size", `${o2}px`), fe.saveDrawerSize(this.position, o2), n = { width: o2 };
        } else if (this.position === "left") {
          const o2 = t;
          this.style.setProperty("--size", `${o2}px`), fe.saveDrawerSize(this.position, o2), n = { width: o2 };
        } else if (this.position === "bottom") {
          const o2 = document.body.clientHeight - i;
          this.style.setProperty("--size", `${o2}px`), fe.saveDrawerSize(this.position, o2), n = { height: o2 };
        }
        se$1.panels.filter((o2) => !o2.floating && o2.panel === this.position).forEach((o2) => {
          se$1.updatePanel(o2.tag, n);
        });
      });
    }, this.sectionPanelDraggingStarted = (e2, t) => {
      this.draggingSectionPanel = e2, y.emit("user-select", { allowSelection: false }), this.draggingSectionPointerStartY = t.clientY, e2.toggleAttribute("dragging", true), e2.style.zIndex = "1000", Array.from(this.querySelectorAll("copilot-section-panel-wrapper")).forEach((i, n) => {
        i.setAttribute(K, `${n}`);
      }), document.addEventListener("mousemove", this.sectionPanelDragging), document.addEventListener("mouseup", this.sectionPanelDraggingFinished);
    }, this.sectionPanelDragging = (e2) => {
      if (!this.draggingSectionPanel)
        return;
      const { clientX: t, clientY: i } = e2;
      if (!gu(this.getBoundingClientRect(), t, i)) {
        this.cleanUpDragging();
        return;
      }
      const n = i - this.draggingSectionPointerStartY;
      this.draggingSectionPanel.style.transform = `translateY(${n}px)`, this.updateSectionPanelPositionsWhileDragging();
    }, this.sectionPanelDraggingFinished = () => {
      if (!this.draggingSectionPanel)
        return;
      y.emit("user-select", { allowSelection: true });
      const e2 = this.getAllPanels().filter(
        (t) => t.hasAttribute(j) && t.panelInfo?.panelOrder !== Number.parseInt(t.getAttribute(j), 10)
      ).map((t) => ({
        tag: t.panelTag,
        order: Number.parseInt(t.getAttribute(j), 10)
      }));
      this.cleanUpDragging(), se$1.updateOrders(e2), document.removeEventListener("mouseup", this.sectionPanelDraggingFinished), document.removeEventListener("mousemove", this.sectionPanelDragging), this.refreshSplit();
    }, this.updateSectionPanelPositionsWhileDragging = () => {
      const e2 = this.draggingSectionPanel.getBoundingClientRect().height;
      this.getAllPanels().sort((t, i) => {
        const n = t.getBoundingClientRect(), o2 = i.getBoundingClientRect(), s = (n.top + n.bottom) / 2, a = (o2.top + o2.bottom) / 2;
        return s - a;
      }).forEach((t, i) => {
        if (t.setAttribute(j, `${i}`), t.panelTag !== this.draggingSectionPanel?.panelTag) {
          const n = Number.parseInt(t.getAttribute(K), 10);
          n > i ? t.style.transform = `translateY(${-e2}px)` : n < i ? t.style.transform = `translateY(${e2}px)` : t.style.removeProperty("transform");
        }
      });
    }, this.panelExpandedListener = (e2) => {
      this.querySelector(`copilot-section-panel-wrapper[paneltag="${e2.detail.panelTag}"]`) && this.refreshSplit();
    };
  }
  static get styles() {
    return [
      J(Pc),
      wl`
        :host {
          --size: 350px;
          --min-size: 20%;
          --max-size: 80%;
          --default-content-height: 300px;
          --transition-duration: var(--duration-2);
          --opening-delay: var(--duration-2);
          --closing-delay: var(--duration-3);
          --hover-size: 18px;
          --initial-position: 0px;
          position: absolute;
          z-index: var(--z-index-drawer);
          transition: translate var(--transition-duration) var(--closing-delay);
        }

        :host([no-transition]),
        :host([no-transition]) .container {
          transition: none;
          -webkit-transition: none;
          -moz-transition: none;
          -o-transition: none;
        }

        :host(:is([position='left'], [position='right'])) {
          width: var(--size);
          min-width: var(--min-size);
          max-width: var(--max-size);
          top: 0;
          bottom: 0;
        }

        :host([position='left']) {
          left: var(--initial-position);
          translate: calc(-100% + var(--hover-size)) 0%;
          padding-right: var(--hover-size);
        }

        :host([position='right']) {
          right: var(--initial-position);
          translate: calc(100% - var(--hover-size)) 0%;
          padding-left: var(--hover-size);
        }

        :host([position='bottom']) {
          height: var(--size);
          min-height: var(--min-size);
          max-height: var(--max-size);
          bottom: var(--initial-position);
          left: 0;
          right: 0;
          translate: 0% calc(100% - var(--hover-size));
          padding-top: var(--hover-size);
        }

        /* The visible container. Needed to have extra space for hover and resize handle outside it. */

        .container {
          display: flex;
          flex-direction: column;
          box-sizing: border-box;
          height: 100%;
          background: var(--background-color);
          -webkit-backdrop-filter: var(--surface-backdrop-filter);
          backdrop-filter: var(--surface-backdrop-filter);
          overflow-y: auto;
          overflow-x: hidden;
          box-shadow: var(--surface-box-shadow-2);
          transition:
            opacity var(--transition-duration) var(--closing-delay),
            visibility calc(var(--transition-duration) * 2) var(--closing-delay);
          opacity: 0;
          /* For accessibility (restored when open) */
          visibility: hidden;
        }

        :host([position='left']) .container {
          border-right: 1px solid var(--surface-border-color);
        }

        :host([position='right']) .container {
          border-left: 1px solid var(--surface-border-color);
        }

        :host([position='bottom']) .container {
          border-top: 1px solid var(--surface-border-color);
        }

        /* Opened state */

        :host(:is([opened], [keepopen])) {
          translate: 0% 0%;
          transition-delay: var(--opening-delay);
          z-index: var(--z-index-opened-drawer);
        }

        :host(:is([opened], [keepopen])) .container {
          transition-delay: var(--opening-delay);
          visibility: visible;
          opacity: 1;
        }

        .drawer-indicator {
          align-items: center;
          border-radius: 9999px;
          box-shadow: inset 0 0 0 1px hsl(0 0% 0% / 0.2);
          color: white;
          display: flex;
          height: 1.75rem;
          justify-content: center;
          overflow: hidden;
          opacity: 1;
          position: absolute;
          transition-delay: 0.5s;
          transition-duration: 0.2s;
          transition-property: opacity;
          width: 1.75rem;
        }

        .drawer-indicator::before {
          animation: 5s swirl linear infinite;
          animation-play-state: running;
          background-image:
            radial-gradient(circle at 50% -10%, hsl(221 100% 55% / 0.6) 0%, transparent 60%),
            radial-gradient(circle at 25% 40%, hsl(303 71% 64%) 0%, transparent 70%),
            radial-gradient(circle at 80% 10%, hsla(262, 38%, 9%, 0.5) 0%, transparent 80%),
            radial-gradient(circle at 110% 50%, hsla(147, 100%, 77%, 1) 20%, transparent 100%);
          content: '';
          inset: 0;
          opacity: 1;
          position: absolute;
          transition: opacity 0.5s;
        }
        :host([attention-required]) .drawer-indicator::before {
          background-image:
            radial-gradient(circle at 50% -10%, hsl(0deg 100% 55% / 60%) 0%, transparent 60%),
            radial-gradient(circle at 25% 40%, hsl(0deg 71% 64%) 0%, transparent 70%),
            radial-gradient(circle at 80% 10%, hsl(0deg 38% 9% / 50%) 0%, transparent 80%),
            radial-gradient(circle at 110% 50%, hsl(0deg 100% 77%) 20%, transparent 100%);
        }
        :host([opened]) .drawer-indicator {
          opacity: 0;
          transition-delay: 0s;
        }

        .drawer-indicator svg {
          height: 0.75rem;
          width: 0.75rem;
          z-index: 1;
        }

        :host([position='right']) .drawer-indicator {
          left: 0.25rem;
          top: calc(50% - 0.875rem);
        }

        :host([position='right']) .drawer-indicator svg {
          margin-inline-start: -0.625rem;
          transform: rotate(-90deg);
        }

        :host([position='left']) .drawer-indicator {
          right: 0.25rem;
          top: calc(50% - 0.875rem);
        }

        :host([position='left']) .drawer-indicator svg {
          margin-inline-end: -0.625rem;
          transform: rotate(90deg);
        }

        :host([position='bottom']) .drawer-indicator {
          left: calc(50% - 0.875rem);
          top: 0.25rem;
        }

        :host([position='bottom']) .drawer-indicator svg {
          margin-top: -0.625rem;
        }

        .resize {
          position: absolute;
          z-index: 10;
          inset: 0;
        }

        :host(:is([position='left'], [position='right'])) .resize {
          width: var(--hover-size);
          cursor: col-resize;
        }

        :host([position='left']) .resize {
          left: auto;
          right: calc(var(--hover-size) * 0.5);
        }

        :host([position='right']) .resize {
          right: auto;
          left: calc(var(--hover-size) * 0.5);
        }

        :host([position='bottom']) .resize {
          height: var(--hover-size);
          bottom: auto;
          top: calc(var(--hover-size) * 0.5);
          cursor: row-resize;
        }

        :host([resizing]) .container {
          /* vaadin-grid (used in the outline) blocks the mouse events */
          pointer-events: none;
        }

        /* Visual indication of the drawer */

        :host::before {
          content: '';
          position: absolute;
          pointer-events: none;
          z-index: -1;
          inset: var(--hover-size);
          transition: opacity var(--transition-duration) var(--closing-delay);
        }

        :host([document-hidden])::before {
          animation: none;
        }

        :host([document-hidden]) .drawer-indicator {
          -webkit-filter: grayscale(100%); /* Chrome, Safari, Opera */
          filter: grayscale(100%);
        }

        :host(:is([opened], [keepopen]))::before {
          transition-delay: var(--opening-delay);
          opacity: 0;
        }
      `
    ];
  }
  connectedCallback() {
    super.connectedCallback(), this.reaction(
      () => se$1.panels,
      () => this.requestUpdate()
    ), this.reaction(
      () => g$1.operationInProgress,
      (t) => {
        t === xu.DragAndDrop && !this.opened && !this.keepOpen ? this.style.setProperty("pointer-events", "none") : this.style.setProperty("pointer-events", "auto");
      }
    ), this.reaction(
      () => se$1.getAttentionRequiredPanelConfiguration(),
      () => {
        const t = se$1.getAttentionRequiredPanelConfiguration();
        t && !t.floating && this.toggleAttribute(au, t.panel === this.position);
      }
    ), this.reaction(
      () => g$1.active,
      () => {
        if (!g$1.active || !Ct$1.isActivationAnimation() || g$1.activatedFrom === "restore" || g$1.activatedFrom === "test")
          return;
        const t = se$1.getAttentionRequiredPanelConfiguration();
        t && !t.floating && t.panel === this.position || (this.addEventListener("transitionend", this.activationAnimationTransitionEndListener), this.toggleAttribute("no-transition", true), this.opened = true, this.style.setProperty("--closing-delay", "var(--duration-1)"), this.style.setProperty("--initial-position", "calc(-1 * (max(var(--size), var(--min-size)) * 1) / 3)"), requestAnimationFrame(() => {
          this.toggleAttribute("no-transition", false), this.opened = false;
        }));
      }
    ), document.addEventListener("mouseup", this.documentMouseUpListener);
    const e2 = fe.getDrawerSize(this.position);
    e2 && this.style.setProperty("--size", `${e2}px`), document.addEventListener("mousemove", this.resizingMouseMoveListener), this.addEventListener("mouseenter", this.mouseEnterListener), y.on("document-activation-change", (t) => {
      this.toggleAttribute("document-hidden", !t.detail.active);
    }), y.on("panel-expanded", this.panelExpandedListener);
  }
  firstUpdated(e2) {
    super.firstUpdated(e2), requestAnimationFrame(() => this.toggleAttribute("no-transition", false)), this.resizeElement.addEventListener("mousedown", (t) => {
      t.button === 0 && (this.resizing = true, g$1.setDrawerResizing(true), this.setAttribute("resizing", ""), y.emit("user-select", { allowSelection: false }));
    });
  }
  updated(e2) {
    super.updated(e2), e2.has("opened") && this.opened && this.hasAttribute(au) && (this.removeAttribute(au), se$1.clearAttention());
  }
  disconnectedCallback() {
    super.disconnectedCallback(), document.removeEventListener("mousemove", this.resizingMouseMoveListener), document.removeEventListener("mouseup", this.documentMouseUpListener), this.removeEventListener("mouseenter", this.mouseEnterListener), y.off("panel-expanded", this.panelExpandedListener);
  }
  /**
   * Cleans up attributes/styles etc... for dragging operations
   * @private
   */
  cleanUpDragging() {
    this.draggingSectionPanel && (g$1.setSectionPanelDragging(false), this.draggingSectionPanel.style.zIndex = "", Array.from(this.querySelectorAll("copilot-section-panel-wrapper")).forEach((e2) => {
      e2.style.removeProperty("transform"), e2.removeAttribute(j), e2.removeAttribute(K);
    }), this.draggingSectionPanel.removeAttribute("dragging"), this.draggingSectionPanel = null);
  }
  getAllPanels() {
    return Array.from(this.querySelectorAll("copilot-section-panel-wrapper"));
  }
  getAllPanelsOrdered() {
    return this.getAllPanels().sort((e2, t) => e2.panelInfo && t.panelInfo ? e2.panelInfo.panelOrder - t.panelInfo.panelOrder : 0);
  }
  /**
   * Closes the drawer and disables mouse enter event for a while.
   */
  forceClose() {
    this.closingForcefully = true, this.opened = false, setTimeout(() => {
      this.closingForcefully = false;
    }, 0.5);
  }
  mouseEnterListener(e2) {
    if (this.closingForcefully || g$1.sectionPanelResizing)
      return;
    document.querySelector("copilot-main").shadowRoot.querySelector("copilot-drawer-panel[opened]") || (this.refreshSplit(), this.opened = true);
  }
  render() {
    return Ue$1`
      <div class="container">
        <slot></slot>
      </div>
      <div class="resize"></div>
      <div class="drawer-indicator">${o.chevronUp}</div>
    `;
  }
  refreshSplit() {
    ht(this.getAllPanelsOrdered(), this);
  }
};
_([
  h({ reflect: true, attribute: true })
], k.prototype, "position", 2);
_([
  h({ reflect: true, type: Boolean })
], k.prototype, "opened", 2);
_([
  h({ reflect: true, type: Boolean })
], k.prototype, "keepOpen", 2);
_([
  h$1(".container")
], k.prototype, "container", 2);
_([
  h$1(".resize")
], k.prototype, "resizeElement", 2);
k = _([
  yl("copilot-drawer-panel")
], k);
var ft = Object.defineProperty, mt = Object.getOwnPropertyDescriptor, Le = (e2, t, i, n) => {
  for (var o2 = n > 1 ? void 0 : n ? mt(t, i) : t, s = e2.length - 1, a; s >= 0; s--)
    (a = e2[s]) && (o2 = (n ? a(t, i, o2) : a(o2)) || o2);
  return n && o2 && ft(t, i, o2), o2;
};
let ie = class extends ht$1 {
  constructor() {
    super(...arguments), this.checked = false;
  }
  static get styles() {
    return wl`
      .switch {
        display: inline-flex;
        align-items: center;
        gap: var(--space-100);
      }

      .switch input {
        height: 0;
        opacity: 0;
        width: 0;
      }

      .slider {
        background-color: var(--gray-300);
        border-radius: 9999px;
        cursor: pointer;
        inset: 0;
        position: absolute;
        transition: 0.4s;
        height: 0.75rem;
        position: relative;
        width: 1.5rem;
        min-width: 1.5rem;
      }

      .slider:before {
        background-color: white;
        border-radius: 50%;
        bottom: 1px;
        content: '';
        height: 0.625rem;
        left: 1px;
        position: absolute;
        transition: 0.4s;
        width: 0.625rem;
      }

      input:checked + .slider {
        background-color: var(--selection-color);
      }

      input:checked + .slider:before {
        transform: translateX(0.75rem);
      }

      label:has(input:focus) {
        outline: 2px solid var(--selection-color);
        outline-offset: 2px;
      }
    `;
  }
  render() {
    return Ue$1`
      <label class="switch">
        <input
          class="feature-toggle"
          id="feature-toggle-${this.id}"
          type="checkbox"
          ?checked="${this.checked}"
          @change=${(e2) => {
      e2.preventDefault(), this.checked = e2.target.checked, this.dispatchEvent(new CustomEvent("on-change"));
    }} />
        <span class="slider"></span>
        ${this.title}
      </label>
    `;
  }
  //  @change=${(e: InputEvent) => this.toggleFeatureFlag(e, feature)}
};
Le([
  h({ reflect: true, type: Boolean })
], ie.prototype, "checked", 2);
ie = Le([
  yl("copilot-toggle-button")
], ie);
class vt {
  constructor() {
    this.offsetX = 0, this.offsetY = 0;
  }
  draggingStarts(t, i) {
    this.offsetX = i.clientX - t.getBoundingClientRect().left, this.offsetY = i.clientY - t.getBoundingClientRect().top;
  }
  dragging(t, i) {
    const n = i.clientX, o2 = i.clientY, s = n - this.offsetX, a = n - this.offsetX + t.getBoundingClientRect().width, h2 = o2 - this.offsetY, m = o2 - this.offsetY + t.getBoundingClientRect().height;
    return this.adjust(t, s, h2, a, m);
  }
  adjust(t, i, n, o2, s) {
    let a, h2, m, v;
    const x = document.documentElement.getBoundingClientRect().width, U = document.documentElement.getBoundingClientRect().height;
    return (o2 + i) / 2 < x / 2 ? (t.style.setProperty("--left", `${i}px`), t.style.setProperty("--right", ""), v = void 0, a = Math.max(0, i)) : (t.style.removeProperty("--left"), t.style.setProperty("--right", `${x - o2}px`), a = void 0, v = Math.max(0, x - o2)), (n + s) / 2 < U / 2 ? (t.style.setProperty("--top", `${n}px`), t.style.setProperty("--bottom", ""), m = void 0, h2 = Math.max(0, n)) : (t.style.setProperty("--top", ""), t.style.setProperty("--bottom", `${U - s}px`), h2 = void 0, m = Math.max(0, U - s)), {
      left: a,
      right: v,
      top: h2,
      bottom: m
    };
  }
  anchor(t) {
    const { left: i, top: n, bottom: o2, right: s } = t.getBoundingClientRect();
    return this.adjust(t, i, n, s, o2);
  }
  anchorLeftTop(t) {
    const { left: i, top: n } = t.getBoundingClientRect();
    return t.style.setProperty("--left", `${i}px`), t.style.setProperty("--right", ""), t.style.setProperty("--top", `${n}px`), t.style.setProperty("--bottom", ""), {
      left: i,
      top: n
    };
  }
}
const P = new vt();
var bt = Object.defineProperty, wt = Object.getOwnPropertyDescriptor, T = (e2, t, i, n) => {
  for (var o2 = n > 1 ? void 0 : n ? wt(t, i) : t, s = e2.length - 1, a; s >= 0; s--)
    (a = e2[s]) && (o2 = (n ? a(t, i, o2) : a(o2)) || o2);
  return n && o2 && bt(t, i, o2), o2;
};
const ve = "https://github.com/JetBrains/JetBrainsRuntime/releases";
function yt(e2, t) {
  if (!t)
    return true;
  const [i, n, o2] = t.split(".").map((m) => parseInt(m)), [s, a, h2] = e2.split(".").map((m) => parseInt(m));
  if (i < s)
    return true;
  if (i === s) {
    if (n < a)
      return true;
    if (n === a)
      return o2 < h2;
  }
  return false;
}
const be = "Download complete";
let w = class extends ql {
  constructor() {
    super(), this.javaPluginSectionOpened = false, this.hotswapSectionOpened = false, this.hotswapTab = "hotswapagent", this.downloadStatusMessages = [], this.downloadProgress = 0, this.onDownloadStatusUpdate = this.downloadStatusUpdate.bind(this), this.reaction(
      () => [g$1.jdkInfo, g$1.idePluginState],
      () => {
        g$1.idePluginState && (!g$1.idePluginState.ide || !g$1.idePluginState.active ? this.javaPluginSectionOpened = true : (!(/* @__PURE__ */ new Set(["vscode", "intellij"])).has(g$1.idePluginState.ide) || !g$1.idePluginState.active) && (this.javaPluginSectionOpened = false)), g$1.jdkInfo && Io() !== "success" && (this.hotswapSectionOpened = true);
      },
      { fireImmediately: true }
    );
  }
  connectedCallback() {
    super.connectedCallback(), y.on("set-up-vs-code-hotswap-status", this.onDownloadStatusUpdate);
  }
  disconnectedCallback() {
    super.disconnectedCallback(), y.off("set-up-vs-code-hotswap-status", this.onDownloadStatusUpdate);
  }
  render() {
    const e2 = {
      intellij: g$1.idePluginState?.ide === "intellij",
      vscode: g$1.idePluginState?.ide === "vscode",
      eclipse: g$1.idePluginState?.ide === "eclipse",
      idePluginInstalled: !!g$1.idePluginState?.active
    };
    return Ue$1`
      <div part="container">${this.renderPluginSection(e2)} ${this.renderHotswapSection(e2)}</div>
      <div part="footer">
        <vaadin-button
          id="close"
          @click="${() => se$1.updatePanel(he.tag, { floating: false })}"
          >Close
        </vaadin-button>
      </div>
    `;
  }
  renderPluginSection(e2) {
    let t = "";
    e2.intellij ? t = "IntelliJ" : e2.vscode ? t = "VS Code" : e2.eclipse && (t = "Eclipse");
    let i, n;
    e2.vscode || e2.intellij ? e2.idePluginInstalled ? (i = `Plugin for ${t} installed`, n = this.renderPluginInstalledContent()) : (i = `Plugin for ${t} not installed`, n = this.renderPluginIsNotInstalledContent(e2)) : e2.eclipse ? (i = "Eclipse development workflow is not supported yet", n = this.renderEclipsePluginContent()) : (i = "No IDE found", n = this.renderNoIdePluginContent());
    const o$1 = e2.idePluginInstalled ? o.checkCircle : o.alertTriangle;
    return Ue$1`
      <details
        part="panel"
        .open=${this.javaPluginSectionOpened}
        @toggle=${(s) => {
      iu(() => {
        this.javaPluginSectionOpened = s.target.open;
      });
    }}>
        <summary part="header">
          <span class="icon ${e2.idePluginInstalled ? "success" : "warning"}">${o$1}</span>
          <div>${i}</div>
        </summary>
        <div part="content">${n}</div>
      </details>
    `;
  }
  renderNoIdePluginContent() {
    return Ue$1`
      <div>
        <div>We could not detect an IDE</div>
        ${this.recommendSupportedPlugin()}
      </div>
    `;
  }
  renderEclipsePluginContent() {
    return Ue$1`
      <div>
        <div>Eclipse workflow environment is not supported yet.</div>
        ${this.recommendSupportedPlugin()}
      </div>
    `;
  }
  recommendSupportedPlugin() {
    return Ue$1`<p>
      Please use <a href="https://code.visualstudio.com">Visual Studio Code</a> or
      <a href="https://www.jetbrains.com/idea">IntelliJ IDEA</a> for better development experience
    </p>`;
  }
  renderPluginInstalledContent() {
    return Ue$1` <p>You have a running plugin. Enjoy your awesome development workflow!</p> `;
  }
  renderPluginIsNotInstalledContent(e2) {
    let t = null, i = "Install from Marketplace";
    return e2.intellij ? (t = su, i = "Install from JetBrains Marketplace") : e2.vscode && (t = lu, i = "Install from VSCode Marketplace"), Ue$1`
      <div>
        <div>Install the Vaadin IDE Plugin to ensure a smooth development workflow</div>
        <p>
          Installing the plugin is not required, but strongly recommended.<br />Some Vaadin Copilot functionality, such
          as undo, will not function optimally without the plugin.
        </p>
        ${t ? Ue$1` <div>
              <vaadin-button
                @click="${() => {
      window.open(t, "_blank");
    }}"
                >${i}
                <vaadin-icon icon="vaadin:external-link"></vaadin-icon>
              </vaadin-button>
            </div>` : O$1}
      </div>
    `;
  }
  renderHotswapSection(e2) {
    const { jdkInfo: t } = g$1;
    if (!t)
      return O$1;
    const i = Io(), n = Vu();
    let o$1, s, a;
    return i === "success" ? (o$1 = o.checkCircle, a = "Java Hotswap is enabled") : i === "warning" ? (o$1 = o.alertTriangle, a = "Java Hotswap is not enabled") : i === "error" && (o$1 = o.alertTriangle, a = "Java Hotswap is partially enabled"), this.hotswapTab === "jrebel" ? t.jrebel ? s = this.renderJRebelInstalledContent() : s = this.renderJRebelNotInstalledContent() : e2.intellij ? s = this.renderHotswapAgentPluginContent(this.renderIntelliJHotswapHint) : e2.vscode ? s = this.renderHotswapAgentPluginContent(this.renderVSCodeHotswapHint) : s = this.renderHotswapAgentNotInstalledContent(e2), Ue$1` <details
      part="panel"
      .open=${this.hotswapSectionOpened}
      @toggle=${(h2) => {
      iu(() => {
        this.hotswapSectionOpened = h2.target.open;
      });
    }}>
      <summary part="header">
        <span class="icon ${i}">${o$1}</span>
        <div>${a}</div>
      </summary>
      <div part="content">
        ${n !== "none" ? Ue$1`${n === "jrebel" ? this.renderJRebelInstalledContent() : this.renderHotswapAgentInstalledContent()}` : Ue$1`
            <div class="tabs" role="tablist">
              <button
                aria-selected="${this.hotswapTab === "hotswapagent" ? "true" : "false"}"
                class="tab"
                role="tab"
                @click=${() => {
      this.hotswapTab = "hotswapagent";
    }}>
                Hotswap Agent
              </button>
              <button
                aria-selected="${this.hotswapTab === "jrebel" ? "true" : "false"}"
                class="tab"
                role="tab"
                @click=${() => {
      this.hotswapTab = "jrebel";
    }}>
                JRebel
              </button>
            </div>
            <div part="content">${s}</div>
            </div>
            </details>
          `}
      </div>
    </details>`;
  }
  renderJRebelNotInstalledContent() {
    return Ue$1`
      <div>
        <a href="https://www.jrebel.com">JRebel ${o.share}</a> is a commercial hotswap solution. Vaadin detects the
        JRebel Agent and automatically reloads the application in the browser after the Java changes have been
        hotpatched.
        <p>
          Go to
          <a href="https://www.jrebel.com/products/jrebel/learn" target="_blank" rel="noopener noreferrer">
            https://www.jrebel.com/products/jrebel/learn ${o.share}</a
          >
          to get started
        </p>
      </div>
    `;
  }
  renderHotswapAgentNotInstalledContent(e2) {
    const t = [
      this.renderJavaRunningInDebugModeSection(),
      this.renderHotswapAgentJdkSection(e2),
      this.renderInstallHotswapAgentJdkSection(e2),
      this.renderHotswapAgentVersionSection(),
      this.renderHotswapAgentMissingArgParam(e2)
    ];
    return Ue$1` <div part="hotswap-agent-section-container">${t}</div> `;
  }
  renderHotswapAgentPluginContent(e2) {
    const i = Io() === "success";
    return Ue$1`
      <div part="hotswap-agent-section-container">
        <div class="inner-section">
          <span class="hotswap icon ${i ? "success" : "warning"}"
            >${i ? o.checkCircle : o.alertTriangle}</span
          >
          ${e2()}
        </div>
      </div>
    `;
  }
  renderIntelliJHotswapHint() {
    return Ue$1` <div class="hint">
      <h3>Use 'Debug using Hotswap Agent' launch configuration</h3>
      Vaadin IntelliJ plugin offers launch mode that does not require any manual configuration!
      <p>
        In order to run recommended launch configuration, you should click three dots right next to Debug button and
        select <code>Debug using Hotswap Agent</code> option.
      </p>
    </div>`;
  }
  renderVSCodeHotswapHint() {
    return Ue$1` <div class="hint">
      <h3>Use 'Debug (hotswap)'</h3>
      With Vaadin Visual Studio Code extension you can run Hotswap Agent without any manual configuration required!
      <p>Click <code>Debug (hotswap)</code> within your main class to debug application using Hotswap Agent.</p>
    </div>`;
  }
  renderJavaRunningInDebugModeSection() {
    const e2 = g$1.jdkInfo?.runningInJavaDebugMode;
    return Ue$1`
      <div class="inner-section">
        <details class="inner" .open="${!e2}">
          <summary>
            <span class="icon ${e2 ? "success" : "warning"}"
              >${e2 ? o.checkCircle : o.alertTriangle}</span
            >
            <div>Run Java in debug mode</div>
          </summary>
          <div class="hint">Start the application in debug mode in the IDE</div>
        </details>
      </div>
    `;
  }
  renderHotswapAgentMissingArgParam(e2) {
    const t = g$1.jdkInfo?.runningWitHotswap && g$1.jdkInfo?.runningWithExtendClassDef;
    return Ue$1`
      <div class="inner-section">
        <details class="inner" .open="${!t}">
          <summary>
            <span class="icon ${t ? "success" : "warning"}"
              >${t ? o.checkCircle : o.alertTriangle}</span
            >
            <div>Enable HotswapAgent</div>
          </summary>
          <div class="hint">
            <ul>
              ${e2.intellij ? Ue$1`<li>Launch as mentioned in the previous step</li>` : O$1}
              ${e2.intellij ? Ue$1`<li>
                    To manually configure IntelliJ, add the
                    <code>-XX:HotswapAgent=fatjar -XX:+AllowEnhancedClassRedefinition -XX:+UpdateClasses</code> JVM
                    arguments when launching the application
                  </li>` : Ue$1`<li>
                    Add the
                    <code>-XX:HotswapAgent=fatjar -XX:+AllowEnhancedClassRedefinition -XX:+UpdateClasses</code> JVM
                    arguments when launching the application
                  </li>`}
            </ul>
          </div>
        </details>
      </div>
    `;
  }
  renderHotswapAgentJdkSection(e2) {
    const t = g$1.jdkInfo?.extendedClassDefCapable, i = this.downloadStatusMessages?.[this.downloadStatusMessages.length - 1] === be;
    return Ue$1`
      <div class="inner-section">
        <details class="inner" .open="${!t}">
          <summary>
            <span class="icon ${t ? "success" : "warning"}"
              >${t ? o.checkCircle : o.alertTriangle}</span
            >
            <div>Run using JetBrains Runtime JDK</div>
          </summary>
          <div class="hint">
            JetBrains Runtime provides much better hotswapping compared to other JDKs.
            <ul>
              ${e2.intellij && yt("1.3.0", g$1.idePluginState?.version) ? Ue$1` <li>Upgrade to the latest IntelliJ plugin</li>` : O$1}
              ${e2.intellij ? Ue$1` <li>Launch the application in IntelliJ using "Debug using Hotswap Agent"</li>` : O$1}
              ${e2.vscode ? Ue$1` <li>
                    <a href @click="${(n) => this.downloadJetbrainsRuntime(n)}"
                      >Let Copilot download and set up JetBrains Runtime for VS Code</a
                    >
                    ${this.downloadProgress > 0 ? Ue$1`<vaadin-progress-bar
                          .value="${this.downloadProgress}"
                          min="0"
                          max="1"></vaadin-progress-bar>` : O$1}
                    <ul>
                      ${this.downloadStatusMessages.map((n) => Ue$1`<li>${n}</li>`)}
                      ${i ? Ue$1`<h3>Go to VS Code and launch the 'Debug using Hotswap Agent' configuration</h3>` : O$1}
                    </ul>
                  </li>` : O$1}
              <li>
                ${e2.intellij || e2.vscode ? Ue$1`If there is a problem, you can manually
                      <a target="_blank" href="${ve}">download JetBrains Runtime JDK</a> and set up
                      your debug configuration to use it.` : Ue$1`<a target="_blank" href="${ve}">Download JetBrains Runtime JDK</a> and set up
                      your debug configuration to use it.`}
              </li>
            </ul>
          </div>
        </details>
      </div>
    `;
  }
  renderInstallHotswapAgentJdkSection(e2) {
    const t = g$1.jdkInfo?.hotswapAgentFound, i = g$1.jdkInfo?.extendedClassDefCapable;
    return Ue$1`
      <div class="inner-section">
        <details class="inner" .open="${!t}">
          <summary>
            <span class="icon ${t ? "success" : "warning"}"
              >${t ? o.checkCircle : o.alertTriangle}</span
            >
            <div>Install HotswapAgent</div>
          </summary>
          <div class="hint">
            Hotswap Agent provides application level support for hot reloading, such as reinitalizing Vaadin @Route or
            @BrowserCallable classes when they are updated
            <ul>
              ${e2.intellij ? Ue$1`<li>Launch as mentioned in the previous step</li>` : O$1}
              ${!e2.intellij && !i ? Ue$1`<li>First install JetBrains Runtime as mentioned in the step above.</li>` : O$1}
              ${e2.intellij ? Ue$1`<li>
                    To manually configure IntelliJ, download HotswapAgent and install the jar file as
                    <code>[JAVA_HOME]/lib/hotswap/hotswap-agent.jar</code> in the JetBrains Runtime JDK. Note that the
                    file must be renamed to exactly match this path.
                  </li>` : Ue$1`<li>
                    Download HotswapAgent and install the jar file as
                    <code>[JAVA_HOME]/lib/hotswap/hotswap-agent.jar</code> in the JetBrains Runtime JDK. Note that the
                    file must be renamed to exactly match this path.
                  </li>`}
            </ul>
          </div>
        </details>
      </div>
    `;
  }
  renderHotswapAgentVersionSection() {
    if (!g$1.jdkInfo?.hotswapAgentFound)
      return O$1;
    const e2 = g$1.jdkInfo?.hotswapVersionOk, t = g$1.jdkInfo?.hotswapVersion, i = g$1.jdkInfo?.hotswapAgentLocation;
    return Ue$1`
      <div class="inner-section">
        <details class="inner" .open="${!e2}">
          <summary>
            <span class="icon ${e2 ? "success" : "warning"}"
              >${e2 ? o.checkCircle : o.alertTriangle}</span
            >
            <div>Hotswap version requires update</div>
          </summary>
          <div class="hint">
            HotswapAgent version ${t} is in use
            <a target="_blank" href="https://github.com/HotswapProjects/HotswapAgent/releases"
              >Download the latest HotswapAgent</a
            >
            and place it in <code>${i}</code>
          </div>
        </details>
      </div>
    `;
  }
  renderJRebelInstalledContent() {
    return Ue$1` <div>JRebel is in use. Enjoy your awesome development workflow!</div> `;
  }
  renderHotswapAgentInstalledContent() {
    return Ue$1` <div>Hotswap agent is in use. Enjoy your awesome development workflow!</div> `;
  }
  async downloadJetbrainsRuntime(e2) {
    return e2.target.disabled = true, e2.preventDefault(), this.downloadStatusMessages = [], hn(`${ve$1}set-up-vs-code-hotswap`, {}, (t) => {
      t.data.error ? (le("Error downloading JetBrains runtime", t.data.error), this.downloadStatusMessages = [...this.downloadStatusMessages, "Download failed"]) : this.downloadStatusMessages = [...this.downloadStatusMessages, be];
    });
  }
  downloadStatusUpdate(e2) {
    const t = e2.detail.progress;
    t ? this.downloadProgress = t : this.downloadStatusMessages = [...this.downloadStatusMessages, e2.detail.message];
  }
};
w.NAME = "copilot-development-setup-user-guide";
w.styles = wl`
    :host {
      --icon-size: 24px;
      --summary-header-gap: 10px;
      --footer-height: calc(50px + var(--space-150));
      color: var(--color);
    }
    :host code {
      background-color: var(--gray-50);
      font-size: var(--font-size-0);
      display: inline-block;
      margin-top: var(--space-100);
      margin-bottom: var(--space-100);
      user-select: all;
    }

    [part='container'] {
      display: flex;
      flex-direction: column;
      gap: var(--space-150);
      padding: var(--space-150);
      box-sizing: border-box;
      height: calc(100% - var(--footer-height));
      overflow: auto;
    }

    [part='footer'] {
      display: flex;
      justify-content: flex-end;
      height: var(--footer-height);
      padding-left: var(--space-150);
      padding-right: var(--space-150);
    }
    [part='hotswap-agent-section-container'] {
      display: flex;
      flex-direction: column;
      gap: var(--space-100);
      position: relative;
    }
    [part='content'] {
      display: flex;
      padding: var(--space-150);
      flex-direction: column;
    }
    div.inner-section div.hint {
      margin-left: calc(var(--summary-header-gap) + var(--icon-size));
      margin-top: var(--space-75);
    }
    details {
      display: flex;
      flex-direction: column;
      box-sizing: border-box;

      & span.icon {
        width: var(--icon-size);
        height: var(--icon-size);
      }
      & span.icon.warning {
        color: var(--lumo-warning-color);
      }
      & span.icon.success {
        color: var(--lumo-success-color);
      }
      & span.hotswap.icon {
        position: absolute;
        top: var(--space-75);
        left: var(--space-75);
      }
      & > summary,
      summary::part(header) {
        display: flex;
        flex-direction: row;
        align-items: center;
        cursor: pointer;
        position: relative;
        gap: var(--summary-header-gap);
        font: var(--font);
      }
      summary::after,
      summary::part(header)::after {
        content: '';
        display: block;
        width: 4px;
        height: 4px;
        border-color: var(--color);
        opacity: var(--panel-toggle-opacity, 0.2);
        border-width: 2px;
        border-style: solid solid none none;
        transform: rotate(var(--panel-toggle-angle, -45deg));
        position: absolute;
        right: 15px;
        top: calc(50% - var(--panel-toggle-offset, 2px));
      }
      &:not([open]) {
        --panel-toggle-angle: 135deg;
        --panel-toggle-offset: 4px;
      }
    }
    details[part='panel'] {
      background: var(--card-bg);
      border: var(--card-border);
      border-radius: 4px;
      user-select: none;

      &:has(summary:hover) {
        border-color: var(--accent-color);
      }

      & > summary,
      summary::part(header) {
        padding: 10px 10px;
        padding-right: 25px;
      }

      summary:hover,
      summary::part(header):hover {
        --panel-toggle-opacity: 0.5;
      }

      input[type='checkbox'],
      summary::part(checkbox) {
        margin: 0;
      }

      &:not([open]):hover {
        background: var(--card-hover-bg);
      }

      &[open] {
        background: var(--card-open-bg);
        box-shadow: var(--card-open-shadow);

        & > summary {
          font-weight: bold;
        }
      }
      .tabs {
        border-bottom: 1px solid var(--border-color);
        box-sizing: border-box;
        display: flex;
        height: 2.25rem;
      }

      .tab {
        background: none;
        border: none;
        border-bottom: 1px solid transparent;
        color: var(--color);
        font: var(--font-button);
        height: 2.25rem;
        padding: 0 0.75rem;
      }

      .tab[aria-selected='true'] {
        color: var(--color-high-contrast);
        border-bottom-color: var(--color-high-contrast);
      }

      .tab-content {
        flex: 1 1 auto;
        gap: var(--space-150);
        overflow: auto;
        padding: var(--space-150);
      }

      h3 {
        margin-top: 0;
      }
    }
  `;
T([
  g()
], w.prototype, "javaPluginSectionOpened", 2);
T([
  g()
], w.prototype, "hotswapSectionOpened", 2);
T([
  g()
], w.prototype, "hotswapTab", 2);
T([
  g()
], w.prototype, "downloadStatusMessages", 2);
T([
  g()
], w.prototype, "downloadProgress", 2);
w = T([
  yl(w.NAME)
], w);
const he = Su({
  header: "Development Workflow",
  tag: Fc,
  width: 800,
  height: 800,
  floatingPosition: {
    top: 50,
    left: 50
  },
  individual: true
}), xt = {
  init(e2) {
    e2.addPanel(he);
  }
};
window.Vaadin.copilot.plugins.push(xt);
se$1.addPanel(he);
var At = (e2, t, i, n) => {
  for (var o2 = t, s = e2.length - 1, a; s >= 0; s--)
    (a = e2[s]) && (o2 = a(o2) || o2);
  return o2;
};
let we = class extends ql {
  createRenderRoot() {
    return this;
  }
  connectedCallback() {
    super.connectedCallback(), this.classList.add("custom-menu-item");
  }
  render() {
    const e2 = Ru(), t = e2.status === "warning" || e2.status === "error";
    return Ue$1`
      <div aria-hidden="true" class="prefix ${t ? e2.status : ""}">${o.lightning}</div>
      <div class="content">
        <span class="label">Development Workflow</span>
        <span class="status ${t ? e2.status : ""}">${e2.message}</span>
      </div>
      <div aria-hidden="true" class="suffix">
        ${t ? Ue$1`<div class="dot ${e2.status}"></div>` : O$1}
      </div>
    `;
  }
};
we = At([
  yl("copilot-activation-button-development-workflow")
], we);
var Ct = (e2, t, i, n) => {
  for (var o2 = t, s = e2.length - 1, a; s >= 0; s--)
    (a = e2[s]) && (o2 = a(o2) || o2);
  return o2;
};
let ye = class extends ql {
  constructor() {
    super(), this.clickListener = this.getClickListener(), this.reaction(
      () => g$1.userInfo,
      () => {
        this.requestUpdate();
      }
    );
  }
  createRenderRoot() {
    return this;
  }
  connectedCallback() {
    super.connectedCallback(), this.classList.add("custom-menu-item"), this.addEventListener("click", this.clickListener);
  }
  disconnectedCallback() {
    super.disconnectedCallback(), this.removeEventListener("click", this.clickListener);
  }
  render() {
    const e2 = this.getStatus();
    return Ue$1`
      <div class="prefix">${this.renderPortrait()}</div>
      <div class="content">
        <span class="label"> ${this.getUsername()} </span>
        ${e2 ? Ue$1` <span class="warning"> ${e2} </span> ` : O$1}
      </div>
      <div aria-hidden="true" class="suffix">${this.renderDot()}</div>
    `;
  }
  getClickListener() {
    return g$1.userInfo?.validLicense ? () => window.open("https://vaadin.com/myaccount", "_blank", "noopener") : () => g$1.setLoginCheckActive(true);
  }
  getUsername() {
    return g$1.userInfo?.firstName ? `${g$1.userInfo.firstName} ${g$1.userInfo.lastName}` : "Log in";
  }
  getStatus() {
    if (g$1.userInfo?.validLicense)
      return g$1.userInfo?.copilotProjectCannotLeaveLocalhost ? "AI Disabled" : void 0;
    if (Oo.active) {
      const e2 = Math.round(Oo.remainingTimeInMillis / 864e5);
      return `Preview expires in ${e2}${e2 === 1 ? " day" : " days"}`;
    }
    if (Oo.expired && !g$1.userInfo?.validLicense)
      return "Preview expired";
    if (!Oo.active && !Oo.expired && !g$1.userInfo?.validLicense)
      return "No valid license available";
  }
  renderPortrait() {
    return g$1.userInfo?.portraitUrl ? Ue$1`<div
        class="portrait"
        style="background-image: url('https://vaadin.com${g$1.userInfo.portraitUrl}')"></div>` : O$1;
  }
  renderDot() {
    return g$1.userInfo?.validLicense ? O$1 : Oo.active || Oo.expired ? Ue$1`<div class="dot warning"></div>` : O$1;
  }
};
ye = Ct([
  yl("copilot-activation-button-user-info")
], ye);
function f(e2) {
  return Me("vaadin-menu-bar-item", e2);
}
function Z(e2) {
  return Me("vaadin-context-menu-item", e2);
}
function Me(e2, t) {
  const i = document.createElement(e2);
  if (t.style && (i.className = t.style), t.icon)
    if (typeof t.icon == "string") {
      const n = document.createElement("vaadin-icon");
      n.setAttribute("icon", t.icon), i.append(n);
    } else
      i.append(xe(t.icon.strings[0]));
  if (t.label) {
    const n = document.createElement("span");
    n.className = "label", n.innerHTML = t.label, i.append(n);
  } else if (t.component) {
    const n = mu(t.component) ? t.component : document.createElement(t.component);
    i.append(n);
  }
  if (t.hint) {
    const n = document.createElement("span");
    n.className = "hint", n.innerHTML = t.hint, i.append(n);
  }
  if (t.suffix)
    if (typeof t.suffix == "string") {
      const n = document.createElement("span");
      n.innerHTML = t.suffix, i.append(n);
    } else
      i.append(xe(t.suffix.strings[0]));
  return i;
}
function xe(e2) {
  if (!e2) return null;
  const t = document.createElement("template");
  t.innerHTML = e2;
  const i = t.content.children;
  return i.length === 1 ? i[0] : i;
}
function _e(e2) {
  return hn("copilot-switch-user", { username: e2 }, (t) => t.data.error ? (Eo({ type: Ve.ERROR, message: "Unable to switch user", details: t.data.error.message }), false) : true);
}
var $t = Object.defineProperty, St = Object.getOwnPropertyDescriptor, O = (e2, t, i, n) => {
  for (var o2 = n > 1 ? void 0 : n ? St(t, i) : t, s = e2.length - 1, a; s >= 0; s--)
    (a = e2[s]) && (o2 = (n ? a(t, i, o2) : a(o2)) || o2);
  return n && o2 && $t(t, i, o2), o2;
};
const kt = 8;
function Dt() {
  const e2 = document.createElement("vaadin-text-field");
  return e2.label = "Username to Switch To", e2.style.width = "100%", e2.autocomplete = "off", e2.addEventListener("click", async (t) => {
    t.stopPropagation();
  }), e2.addEventListener("keydown", async (t) => {
    if (t.stopPropagation(), t.key === "Enter") {
      const i = e2.value;
      await _e(i) && (Ct$1.addRecentSwitchedUsername(i), window.location.reload());
    }
  }), e2;
}
let ne = class extends ql {
  constructor() {
    super(...arguments), this.username = "";
  }
  connectedCallback() {
    super.connectedCallback(), this.style.display = "contents";
  }
  render() {
    return Ue$1`<span style="flex: 1;  display: flex; justify-content: space-between;"
      ><span>${this.username}</span
      ><span
        @click=${(e2) => {
      Ct$1.removeRecentSwitchedUsername(this.username), e2.stopPropagation();
      const t = this.parentElement;
      if (t.style.display = "none", Ct$1.getRecentSwitchedUsernames().length === 0) {
        const i = t.parentElement?.firstElementChild;
        i && (i.style.display = "none");
      }
    }}
        >${o.trash}</span
      ></span
    >`;
  }
};
O([
  h({ type: String })
], ne.prototype, "username", 2);
ne = O([
  yl("copilot-switch-user")
], ne);
function Rt(e2) {
  const t = document.createElement("copilot-switch-user");
  return t.username = e2, t;
}
let N = class extends ql {
  constructor() {
    super(...arguments), this.initialMouseDownPosition = null, this.dragging = false, this.items = [], this.mouseDownListener = (e2) => {
      this.initialMouseDownPosition = { x: e2.clientX, y: e2.clientY }, P.draggingStarts(this, e2), document.addEventListener("mousemove", this.documentDraggingMouseMoveEventListener);
    }, this.documentDraggingMouseMoveEventListener = (e2) => {
      if (this.initialMouseDownPosition && !this.dragging) {
        const { clientX: t, clientY: i } = e2;
        this.dragging = Math.abs(t - this.initialMouseDownPosition.x) + Math.abs(i - this.initialMouseDownPosition.y) > kt;
      }
      this.dragging && (this.setOverlayVisibility(false), P.dragging(this, e2));
    }, this.documentMouseUpListener = (e2) => {
      if (this.initialMouseDownPosition = null, document.removeEventListener("mousemove", this.documentDraggingMouseMoveEventListener), this.dragging) {
        const t = P.dragging(this, e2);
        Ct$1.setActivationButtonPosition(t), this.setOverlayVisibility(true);
      } else
        this.setMenuBarOnClick();
      this.dragging = false;
    }, this.closeMenuMouseMoveListener = (e2) => {
      e2.composedPath().some((n) => {
        if (n instanceof HTMLElement) {
          const o2 = n;
          if (o2.localName === this.localName || o2.localName === "vaadin-menu-bar-overlay" && o2.classList.contains("activation-button-menu"))
            return true;
        }
        return this.checkPointerIsInRangeInSurroundingRectangle(e2);
      }) ? this.closeMenuWithDebounce.clear() : this.closeMenuWithDebounce();
    }, this.closeMenuWithDebounce = Uc(() => {
      this.closeMenu();
    }, 250), this.checkPointerIsInRangeInSurroundingRectangle = (e2) => {
      const i = document.querySelector("copilot-main")?.shadowRoot?.querySelectorAll("vaadin-menu-bar-overlay.activation-button-menu"), n = this.menubar;
      return i ? Array.from(i).some((o2) => {
        const s = o2.querySelector("vaadin-menu-bar-list-box");
        if (!s)
          return false;
        const a = s.getBoundingClientRect(), h2 = n.getBoundingClientRect(), m = Math.min(a.x, h2.x), v = Math.min(a.y, h2.y), x = Math.max(a.width, h2.width), U = a.height + h2.height;
        return gu(new DOMRect(m, v, x, U), e2.clientX, e2.clientY);
      }) : false;
    }, this.dispatchSpotlightActivationEvent = (e2) => {
      this.dispatchEvent(
        new CustomEvent("spotlight-activation-changed", {
          detail: e2
        })
      );
    }, this.activationBtnClicked = (e2) => {
      if (g$1.active && this.handleAttentionRequiredOnClick()) {
        e2?.stopPropagation(), e2?.preventDefault();
        return;
      }
      e2?.stopPropagation(), this.dispatchEvent(new CustomEvent("activation-btn-clicked"));
    }, this.handleAttentionRequiredOnClick = () => {
      const e2 = se$1.getAttentionRequiredPanelConfiguration();
      return e2 ? e2.panel && !e2.floating ? (y.emit("open-attention-required-drawer", null), true) : (se$1.clearAttention(), true) : false;
    }, this.closeMenu = () => {
      this.menubar._close(), document.removeEventListener("mousemove", this.closeMenuMouseMoveListener);
    }, this.setMenuBarOnClick = () => {
      const e2 = this.shadowRoot.querySelector("vaadin-menu-bar-button");
      e2 && (e2.onclick = this.activationBtnClicked);
    };
  }
  static get styles() {
    return [
      J(Pc),
      wl`
        :host {
          --space: 8px;
          --height: 28px;
          --width: 28px;
          position: absolute;
          top: clamp(var(--space), var(--top), calc(100vh - var(--height) - var(--space)));
          left: clamp(var(--space), var(--left), calc(100vw - var(--width) - var(--space)));
          bottom: clamp(var(--space), var(--bottom), calc(100vh - var(--height) - var(--space)));
          right: clamp(var(--space), var(--right), calc(100vw - var(--width) - var(--space)));
          user-select: none;
          -ms-user-select: none;
          -moz-user-select: none;
          -webkit-user-select: none;
          --indicator-color: var(--red);
          /* Don't add a z-index or anything else that creates a stacking context */
        }

        :host .menu-button {
          min-width: unset;
        }

        :host([document-hidden]) {
          -webkit-filter: grayscale(100%); /* Chrome, Safari, Opera */
          filter: grayscale(100%);
        }

        .menu-button::part(container) {
          overflow: visible;
        }

        .menu-button vaadin-menu-bar-button {
          all: initial;
          display: block;
          position: relative;
          z-index: var(--z-index-activation-button);
          width: var(--width);
          height: var(--height);
          overflow: hidden;
          color: transparent;
          background: hsl(0 0% 0% / 0.25);
          border-radius: 8px;
          box-shadow: 0 0 0 1px hsl(0 0% 100% / 0.1);
          cursor: default;
          -webkit-backdrop-filter: blur(8px);
          backdrop-filter: blur(8px);
          transition:
            box-shadow 0.2s,
            background-color 0.2s;
        }

        /* visual effect when active */

        .menu-button vaadin-menu-bar-button::before {
          all: initial;
          content: '';
          background-image:
            radial-gradient(circle at 50% -10%, hsl(221 100% 55% / 0.6) 0%, transparent 60%),
            radial-gradient(circle at 25% 40%, hsl(303 71% 64%) 0%, transparent 70%),
            radial-gradient(circle at 80% 10%, hsla(262, 38%, 9%, 0.5) 0%, transparent 80%),
            radial-gradient(circle at 110% 50%, hsla(147, 100%, 77%, 1) 20%, transparent 100%);
          animation: 5s swirl linear infinite;
          animation-play-state: paused;
          inset: -6px;
          opacity: 0;
          position: absolute;
          transition: opacity 0.5s;
        }

        /* vaadin symbol */

        .menu-button vaadin-menu-bar-button::after {
          all: initial;
          content: '';
          position: absolute;
          inset: 1px;
          background: url('data:image/svg+xml;utf8,<svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M12.7407 9.70401C12.7407 9.74417 12.7378 9.77811 12.7335 9.81479C12.7111 10.207 12.3897 10.5195 11.9955 10.5195C11.6014 10.5195 11.2801 10.209 11.2577 9.8169C11.2534 9.7801 11.2504 9.74417 11.2504 9.70401C11.2504 9.31225 11.1572 8.90867 10.2102 8.90867H7.04307C5.61481 8.90867 5 8.22698 5 6.86345V5.70358C5 5.31505 5.29521 5 5.68008 5C6.06495 5 6.35683 5.31505 6.35683 5.70358V6.09547C6.35683 6.53423 6.655 6.85413 7.307 6.85413H10.4119C11.8248 6.85413 11.9334 7.91255 11.98 8.4729H12.0111C12.0577 7.91255 12.1663 6.85413 13.5791 6.85413H16.6841C17.3361 6.85413 17.614 6.54529 17.614 6.10641L17.6158 5.70358C17.6158 5.31505 17.9246 5 18.3095 5C18.6943 5 19 5.31505 19 5.70358V6.86345C19 8.22698 18.3763 8.90867 16.9481 8.90867H13.7809C12.8338 8.90867 12.7407 9.31225 12.7407 9.70401Z" fill="white"/><path d="M12.7536 17.7785C12.6267 18.0629 12.3469 18.2608 12.0211 18.2608C11.6907 18.2608 11.4072 18.0575 11.2831 17.7668C11.2817 17.7643 11.2803 17.7619 11.279 17.7595C11.2761 17.7544 11.2732 17.7495 11.2704 17.744L8.45986 12.4362C8.3821 12.2973 8.34106 12.1399 8.34106 11.9807C8.34106 11.4732 8.74546 11.0603 9.24238 11.0603C9.64162 11.0603 9.91294 11.2597 10.0985 11.6922L12.0216 15.3527L13.9468 11.6878C14.1301 11.2597 14.4014 11.0603 14.8008 11.0603C15.2978 11.0603 15.7021 11.4732 15.7021 11.9807C15.7021 12.1399 15.6611 12.2973 15.5826 12.4374L12.7724 17.7446C12.7683 17.7524 12.7642 17.7597 12.7601 17.767C12.7579 17.7708 12.7557 17.7746 12.7536 17.7785Z" fill="white"/></svg>');
          background-size: 100%;
        }

        .menu-button vaadin-menu-bar-button[focus-ring] {
          outline: 2px solid var(--selection-color);
          outline-offset: 2px;
        }

        .menu-button vaadin-menu-bar-button:hover {
          background: hsl(0 0% 0% / 0.8);
          box-shadow:
            0 0 0 1px hsl(0 0% 100% / 0.1),
            0 2px 8px -1px hsl(0 0% 0% / 0.3);
        }

        :host([active]) .menu-button vaadin-menu-bar-button {
          background-color: transparent;
          box-shadow:
            inset 0 0 0 1px hsl(0 0% 0% / 0.2),
            0 2px 8px -1px hsl(0 0% 0% / 0.3);
        }

        :host([active]) .menu-button vaadin-menu-bar-button::before {
          opacity: 1;
          animation-play-state: running;
        }

        :host([attention-required]) {
          animation: bounce 0.5s;
          animation-iteration-count: 2;
        }

        [part='indicator'] {
          top: -6px;
          right: -6px;
          width: 12px;
          height: 12px;
          box-sizing: border-box;
          border-radius: 100%;
          position: absolute;
          display: var(--indicator-display, none);
          background: var(--indicator-color);
          z-index: calc(var(--z-index-activation-button) + 1);
          border: 2px solid var(--indicator-border);
        }

        :host([indicator='warning']) {
          --indicator-display: block;
          --indicator-color: var(--yellow);
        }

        :host([indicator='error']) {
          --indicator-display: block;
          --indicator-color: var(--red);
        }
      `
    ];
  }
  connectedCallback() {
    super.connectedCallback(), this.reaction(
      () => se$1.attentionRequiredPanelTag,
      () => {
        this.toggleAttribute(au, se$1.attentionRequiredPanelTag !== null), this.updateIndicator();
      }
    ), this.reaction(
      () => g$1.active,
      () => {
        this.toggleAttribute("active", g$1.active);
      },
      { fireImmediately: true }
    ), this.addEventListener("mousedown", this.mouseDownListener), document.addEventListener("mouseup", this.documentMouseUpListener);
    const e2 = Ct$1.getActivationButtonPosition();
    e2 ? (this.style.setProperty("--left", `${e2.left}px`), this.style.setProperty("--bottom", `${e2.bottom}px`), this.style.setProperty("--right", `${e2.right}px`), this.style.setProperty("--top", `${e2.top}px`)) : (this.style.setProperty("--bottom", "var(--space)"), this.style.setProperty("--right", "var(--space)")), y.on("document-activation-change", (t) => {
      this.toggleAttribute("document-hidden", !t.detail.active);
    }), this.reaction(
      () => [g$1.jdkInfo, g$1.idePluginState],
      () => {
        this.updateIndicator();
      }
    ), this.reaction(
      () => [
        g$1.active,
        g$1.idePluginState,
        Ct$1.isActivationAnimation(),
        Ct$1.isActivationShortcut(),
        Ct$1.isSendErrorReportsAllowed(),
        Ct$1.isAIUsageAllowed(),
        Ct$1.getDismissedNotifications()
      ],
      () => {
        this.generateItems();
      }
    );
  }
  disconnectedCallback() {
    super.disconnectedCallback(), this.removeEventListener("mousedown", this.mouseDownListener), document.removeEventListener("mouseup", this.documentMouseUpListener);
  }
  updateIndicator() {
    if (this.hasAttribute(au)) {
      this.setAttribute("indicator", "error");
      return;
    }
    const e2 = Ru();
    e2.status !== "success" ? this.setAttribute("indicator", e2.status) : this.removeAttribute("indicator");
  }
  /**
   * To hide overlay while dragging
   * @param visible
   */
  setOverlayVisibility(e2) {
    const t = this.shadowRoot.querySelector("vaadin-menu-bar-button").__overlay;
    e2 ? (t?.style.setProperty("display", "flex"), t?.style.setProperty("visibility", "visible")) : (t?.style.setProperty("display", "none"), t?.style.setProperty("visibility", "invisible"));
  }
  generateItems() {
    const e$1 = g$1.active, t = e$1 && !!g$1.idePluginState?.supportedActions?.find((o2) => o2 === "undo"), i = [];
    if (e.springSecurityEnabled) {
      const o2 = Ct$1.getRecentSwitchedUsernames();
      i.push(
        ...o2.map((s) => ({
          component: f({ component: Rt(s) }),
          action: async () => {
            await _e(s) && window.location.reload();
          }
        }))
      ), i.length > 0 && i.unshift({
        component: f({ label: "Recently Used Usernames" }),
        disabled: true
      });
    }
    const n = [
      {
        text: "Vaadin Copilot",
        children: [
          { visible: e$1, component: f({ component: "copilot-activation-button-user-info" }) },
          { visible: e$1, component: "hr" },
          {
            component: f({ component: "copilot-activation-button-development-workflow" }),
            action: Iu
          },
          { visible: e$1, component: "hr" },
          {
            visible: e.springSecurityEnabled,
            component: f({
              icon: o.user,
              label: "Application's User"
            }),
            children: [
              ...i,
              {
                component: f({ component: Dt() })
              }
            ]
          },
          {
            component: "hr",
            visible: e$1
          },
          {
            visible: t,
            component: f({
              icon: o.flipBack,
              label: "Undo",
              hint: ju.undo
            }),
            action: () => {
              y.emit("undoRedo", { undo: true });
            }
          },
          {
            visible: t,
            component: f({
              icon: o.flipForward,
              label: "Redo",
              hint: ju.redo
            }),
            action: () => {
              y.emit("undoRedo", { undo: false });
            }
          },
          {
            component: f({
              icon: o.starsAlt,
              label: "Toggle Command Window",
              hint: ju.toggleCommandWindow,
              style: "toggle-spotlight"
            }),
            action: () => {
              g$1.setSpotlightActive(!g$1.spotlightActive);
            }
          },
          {
            component: "hr",
            visible: e$1
          },
          {
            visible: e$1,
            component: f({
              icon: o.settings,
              label: "Settings"
            }),
            children: [
              {
                component: f({
                  icon: o.keyboard,
                  label: "Activation Shortcut",
                  suffix: Ct$1.isActivationShortcut() ? '<div aria-hidden="true" class="switch on"></div>' : '<div aria-hidden="true" class="switch off"></div>'
                }),
                keepOpen: true,
                action: (o2) => {
                  Ct$1.setActivationShortcut(!Ct$1.isActivationShortcut()), Q(o2, Ct$1.isActivationShortcut());
                }
              },
              {
                component: f({
                  icon: o.play,
                  label: "Activation Animation",
                  suffix: Ct$1.isActivationAnimation() ? '<div aria-hidden="true" class="switch on"></div>' : '<div aria-hidden="true" class="switch off"></div>'
                }),
                keepOpen: true,
                action: (o2) => {
                  Ct$1.setActivationAnimation(!Ct$1.isActivationAnimation()), Q(o2, Ct$1.isActivationAnimation());
                }
              },
              {
                component: f({
                  icon: o.starsAlt,
                  label: "AI Usage",
                  hint: Te()
                }),
                keepOpen: true,
                action: (o2) => {
                  let s;
                  const a = Ct$1.isAIUsageAllowed();
                  a === "ask" ? s = "yes" : a === "no" ? s = "ask" : s = "no", Ct$1.setAIUsageAllowed(s), zt(o2);
                }
              },
              {
                component: f({
                  icon: o.alertCircle,
                  label: "Report Errors to Vaadin",
                  suffix: Ct$1.isSendErrorReportsAllowed() ? '<div aria-hidden="true" class="switch on"></div>' : '<div aria-hidden="true" class="switch off"></div>'
                }),
                keepOpen: true,
                action: (o2) => {
                  Ct$1.setSendErrorReportsAllowed(!Ct$1.isSendErrorReportsAllowed()), Q(o2, Ct$1.isSendErrorReportsAllowed());
                }
              },
              { component: "hr" },
              {
                visible: e$1,
                component: f({
                  icon: o.annotation,
                  label: "Show Welcome Message"
                }),
                keepOpen: true,
                action: () => {
                  g$1.setWelcomeActive(true), g$1.setSpotlightActive(true);
                }
              },
              {
                visible: e$1,
                component: f({
                  icon: o.keyboard,
                  label: "Show Keyboard Shortcuts"
                }),
                action: () => {
                  se$1.updatePanel("copilot-shortcuts-panel", {
                    floating: true
                  });
                }
              },
              {
                visible: Ct$1.getDismissedNotifications().length > 0,
                component: f({
                  icon: o.annotationX,
                  label: "Clear Dismissed Notifications"
                }),
                action: () => {
                  Ct$1.clearDismissedNotifications();
                }
              }
            ]
          },
          { component: "hr" },
          {
            visible: e$1,
            component: f({
              icon: o.annotation,
              label: "Tell Us What You Think"
              // Label used also in ScreenshotsIT.java
            }),
            action: () => {
              se$1.updatePanel("copilot-feedback-panel", {
                floating: true
              });
            }
          },
          {
            component: f({
              icon: o.vaadinLogo,
              label: "Copilot",
              hint: Ct$1.isActivationShortcut() ? ju.toggleCopilot : void 0,
              suffix: g$1.active ? '<div aria-hidden="true" class="switch on"></div>' : '<div aria-hidden="true" class="switch off"></div>'
            }),
            action: () => {
              this.activationBtnClicked();
            }
          }
        ]
      }
    ];
    this.items = n.filter(rl);
  }
  render() {
    return Ue$1`
      <vaadin-menu-bar
        class="menu-button"
        .items="${this.items}"
        ._menuItemsChanged=${function(e2, t, i) {
      if (!t || !i)
        return;
      const n = this;
      e2 !== n._oldItems && (n._oldItems = e2, n.__renderButtons(e2));
    }}
        @item-selected="${(e2) => {
      this.handleMenuItemClick(e2.detail.value);
    }}"
        ?open-on-hover=${!this.dragging}
        @mouseenter="${() => {
      document.addEventListener("mousemove", this.closeMenuMouseMoveListener, { once: true });
    }}"
        overlay-class="activation-button-menu">
      </vaadin-menu-bar>
      <div part="indicator"></div>
    `;
  }
  handleMenuItemClick(e2) {
    e2.action && (e2.action(e2), e2.keepOpen && Et());
  }
  firstUpdated() {
    this.setMenuBarOnClick(), u(this.shadowRoot);
  }
};
O([
  h$1("vaadin-menu-bar")
], N.prototype, "menubar", 2);
O([
  g()
], N.prototype, "dragging", 2);
O([
  g()
], N.prototype, "items", 2);
N = O([
  yl("copilot-activation-button")
], N);
function Et() {
  document.querySelector("copilot-main").shadowRoot.querySelectorAll("vaadin-menu-bar-overlay").forEach((e2) => {
    e2.positionTarget = void 0;
  });
}
function Q(e2, t) {
  const i = e2.component;
  if (!i || typeof i == "string") {
    console.error("Unable to set switch value for a non-component item");
    return;
  }
  const n = i.querySelector(".switch");
  if (!n) {
    console.error("No element found when setting switch value");
    return;
  }
  t ? (n.classList.remove("off"), n.classList.add("on")) : (n.classList.add("off"), n.classList.remove("on"));
}
function zt(e2) {
  const t = e2.component;
  if (!t || typeof t == "string") {
    console.error("Unable to set switch value for a non-component item");
    return;
  }
  const i = t.querySelector(".hint");
  if (!i) {
    console.error("No element found when setting switch value");
    return;
  }
  i.innerText = Te();
}
function Te() {
  return Ct$1.isAIUsageAllowed() === "ask" ? "Always Ask" : Ct$1.isAIUsageAllowed() === "no" ? "Disabled" : "Enabled";
}
var Lt = Object.defineProperty, Mt = Object.getOwnPropertyDescriptor, H = (e2, t, i, n) => {
  for (var o2 = n > 1 ? void 0 : n ? Mt(t, i) : t, s = e2.length - 1, a; s >= 0; s--)
    (a = e2[s]) && (o2 = (n ? a(t, i, o2) : a(o2)) || o2);
  return n && o2 && Lt(t, i, o2), o2;
};
const b = "resize-dir", ee = "floating-resizing-active";
let D = class extends ql {
  constructor() {
    super(...arguments), this.panelTag = "", this.dockingItems = [
      {
        component: Z({
          icon: o.layoutRight,
          label: "Dock right"
        }),
        panel: "right"
      },
      {
        component: Z({
          icon: o.layoutLeft,
          label: "Dock left"
        }),
        panel: "left"
      },
      {
        component: Z({
          icon: o.layoutBottom,
          label: "Dock bottom"
        }),
        panel: "bottom"
      }
    ], this.floatingResizingStarted = false, this.resizingInDrawerStarted = false, this.toggling = false, this.rectangleBeforeResizing = null, this.floatingResizeHandlerMouseMoveListener = (e2) => {
      if (!this.panelInfo?.floating || this.floatingResizingStarted || !this.panelInfo?.expanded)
        return;
      const t = this.getBoundingClientRect(), i = Math.abs(e2.clientX - t.x), n = Math.abs(t.x + t.width - e2.clientX), o2 = Math.abs(e2.clientY - t.y), s = Math.abs(t.y + t.height - e2.clientY), a = Number.parseInt(
        window.getComputedStyle(this).getPropertyValue("--floating-offset-resize-threshold"),
        10
      );
      let h2 = "";
      i < a ? o2 < a ? (h2 = "nw-resize", this.setAttribute(b, "top left")) : s < a ? (h2 = "sw-resize", this.setAttribute(b, "bottom left")) : (h2 = "col-resize", this.setAttribute(b, "left")) : n < a ? o2 < a ? (h2 = "ne-resize", this.setAttribute(b, "top right")) : s < a ? (h2 = "se-resize", this.setAttribute(b, "bottom right")) : (h2 = "col-resize", this.setAttribute(b, "right")) : s < a ? (h2 = "row-resize", this.setAttribute(b, "bottom")) : o2 < a && (h2 = "row-resize", this.setAttribute(b, "top")), h2 !== "" ? (this.rectangleBeforeResizing = this.getBoundingClientRect(), this.style.setProperty("--resize-cursor", h2)) : (this.style.removeProperty("--resize-cursor"), this.removeAttribute(b)), this.toggleAttribute(ee, h2 !== "");
    }, this.floatingResizingMouseDownListener = (e2) => {
      if (!this.hasAttribute(ee) || e2.button !== 0)
        return;
      e2.stopPropagation(), e2.preventDefault(), P.anchorLeftTop(this), this.floatingResizingStarted = true, this.toggleAttribute("resizing", true);
      const t = this.getResizeDirections(), { clientX: i, clientY: n } = e2;
      (t.includes("top") || t.includes("bottom")) && this.style.setProperty("--section-height", null), t.forEach((o2) => this.setResizePosition(o2, i, n)), g$1.setSectionPanelResizing(true);
    }, this.floatingResizingMouseLeaveListener = () => {
      this.panelInfo?.floating && (this.floatingResizingStarted || (this.removeAttribute("resizing"), this.removeAttribute(ee), this.removeAttribute("dragging"), this.style.removeProperty("--resize-cursor"), this.removeAttribute(b)));
    }, this.floatingResizingMouseMoveListener = (e2) => {
      if (!this.panelInfo?.floating || !this.floatingResizingStarted)
        return;
      e2.stopPropagation(), e2.preventDefault();
      const t = this.getResizeDirections(), { clientX: i, clientY: n } = e2;
      t.forEach((o2) => this.setResizePosition(o2, i, n));
    }, this.setFloatingResizeDirectionProps = (e2, t, i, n) => {
      i && i > Number.parseFloat(window.getComputedStyle(this).getPropertyValue("--min-width")) && (this.style.setProperty(`--${e2}`, `${t}px`), this.style.setProperty("width", `${i}px`));
      const o2 = window.getComputedStyle(this), s = Number.parseFloat(o2.getPropertyValue("--header-height")), a = Number.parseFloat(o2.getPropertyValue("--floating-offset-resize-threshold")) / 2;
      n && n > s + a && (this.style.setProperty(`--${e2}`, `${t}px`), this.style.setProperty("height", `${n}px`), this.container.style.setProperty("margin-top", "calc(var(--floating-offset-resize-threshold) / 4)"), this.container.style.height = `calc(${n}px - var(--floating-offset-resize-threshold) / 2)`);
    }, this.floatingResizingMouseUpListener = (e2) => {
      if (!this.floatingResizingStarted || !this.panelInfo?.floating)
        return;
      e2.stopPropagation(), e2.preventDefault(), this.floatingResizingStarted = false, g$1.setSectionPanelResizing(false);
      const { width: t, height: i } = this.getBoundingClientRect(), { left: n, top: o2, bottom: s, right: a } = P.anchor(this), h2 = window.getComputedStyle(this.container), m = Number.parseInt(h2.borderTopWidth, 10), v = Number.parseInt(h2.borderBottomWidth, 10);
      se$1.updatePanel(this.panelInfo.tag, {
        width: t,
        height: i - (m + v),
        floatingPosition: {
          ...this.panelInfo.floatingPosition,
          left: n,
          top: o2,
          bottom: s,
          right: a
        }
      }), this.style.removeProperty("width"), this.style.removeProperty("height"), this.container.style.removeProperty("height"), this.container.style.removeProperty("margin-top"), this.setCssSizePositionProperties(), this.toggleAttribute("dragging", false);
    }, this.transitionEndEventListener = () => {
      this.toggling && (this.toggling = false, P.anchor(this));
    }, this.sectionPanelMouseEnterListener = () => {
      this.hasAttribute(au) && (this.removeAttribute(au), se$1.clearAttention());
    }, this.contentAreaMouseDownListener = () => {
      se$1.bringToFront(this.panelInfo.tag);
    }, this.documentMouseUpEventListener = () => {
      document.removeEventListener("mousemove", this.draggingEventListener), this.panelInfo?.floating && (this.toggleAttribute("dragging", false), g$1.setSectionPanelDragging(false));
    }, this.panelHeaderMouseDownEventListener = (e2) => {
      e2.button === 0 && (se$1.bringToFront(this.panelInfo.tag), !this.hasAttribute(b) && (e2.target instanceof HTMLButtonElement && e2.target.getAttribute("part") === "title-button" ? this.startDraggingDebounce(e2) : this.startDragging(e2)));
    }, this.panelHeaderMouseUpEventListener = (e2) => {
      e2.button === 0 && this.startDraggingDebounce.clear();
    }, this.startDragging = (e2) => {
      P.draggingStarts(this, e2), document.addEventListener("mousemove", this.draggingEventListener), g$1.setSectionPanelDragging(true), this.panelInfo?.floating ? this.toggleAttribute("dragging", true) : this.parentElement.sectionPanelDraggingStarted(this, e2), e2.preventDefault(), e2.stopPropagation();
    }, this.startDraggingDebounce = Uc(this.startDragging, 200), this.draggingEventListener = (e2) => {
      const t = P.dragging(this, e2);
      if (this.panelInfo?.floating && this.panelInfo?.floatingPosition) {
        e2.preventDefault();
        const { left: i, top: n, bottom: o2, right: s } = t;
        se$1.updatePanel(this.panelInfo.tag, {
          floatingPosition: {
            ...this.panelInfo.floatingPosition,
            left: i,
            top: n,
            bottom: o2,
            right: s
          }
        });
      }
    }, this.setCssSizePositionProperties = () => {
      const e2 = se$1.getPanelByTag(this.panelTag);
      if (e2 && (e2.height !== void 0 && (this.panelInfo?.floating || e2.panel === "left" || e2.panel === "right" ? this.style.setProperty("--section-height", `${e2.height}px`) : this.style.removeProperty("--section-height")), e2.width !== void 0 && (e2.floating || e2.panel === "bottom" ? this.style.setProperty("--section-width", `${e2.width}px`) : this.style.removeProperty("--section-width")), e2.floating && e2.floatingPosition && !this.toggling)) {
        const { left: t, top: i, bottom: n, right: o2 } = e2.floatingPosition;
        this.style.setProperty("--left", t !== void 0 ? `${t}px` : "auto"), this.style.setProperty("--top", i !== void 0 ? `${i}px` : "auto"), this.style.setProperty("--bottom", n !== void 0 ? `${n}px` : ""), this.style.setProperty("--right", o2 !== void 0 ? `${o2}px` : "");
        const s = window.getComputedStyle(this);
        parseInt(s.top, 10) < 0 && this.style.setProperty("--top", "0px"), parseInt(s.bottom, 10) < 0 && this.style.setProperty("--bottom", "0px");
      }
    }, this.renderPopupButton = () => {
      if (!this.panelInfo)
        return O$1;
      let e2;
      return this.panelInfo.panel === void 0 ? e2 = "Close the popup" : e2 = this.panelInfo.floating ? `Dock ${this.panelInfo.header} to ${this.panelInfo.panel}` : `Open ${this.panelInfo.header} as a popup`, Ue$1`
      <vaadin-context-menu .items=${this.dockingItems} @item-selected="${this.changeDockingPanel}">
        <button
          @click="${(t) => this.changePanelFloating(t)}"
          @mousedown="${(t) => t.stopPropagation()}"
          aria-label=${e2}
          class="icon"
          part="popup-button"
          title="${e2}">
          ${this.getPopupButtonIcon()}
        </button>
      </vaadin-context-menu>
    `;
    }, this.changePanelFloating = (e2) => {
      if (this.panelInfo)
        if (e2.stopPropagation(), Pu(this), this.panelInfo?.floating)
          se$1.updatePanel(this.panelInfo.tag, { floating: false });
        else {
          let t;
          if (this.panelInfo.floatingPosition)
            t = this.panelInfo.floatingPosition;
          else {
            const { left: o2, top: s } = this.getBoundingClientRect();
            t = {
              left: o2,
              top: s
            };
          }
          let i = this.panelInfo?.height;
          i === void 0 && this.panelInfo.expanded && (i = Number.parseInt(window.getComputedStyle(this).height, 10)), this.parentElement.forceClose(), se$1.updatePanel(this.panelInfo.tag, {
            floating: true,
            expanded: true,
            width: this.panelInfo?.width || Number.parseInt(window.getComputedStyle(this).width, 10),
            height: i,
            floatingPosition: t
          }), se$1.bringToFront(this.panelInfo.tag);
        }
    }, this.toggleExpand = (e2) => {
      this.panelInfo && (e2.stopPropagation(), P.anchorLeftTop(this), se$1.updatePanel(this.panelInfo.tag, {
        expanded: !this.panelInfo.expanded
      }), this.toggling = true, this.toggleAttribute("expanded", this.panelInfo.expanded), y.emit("panel-expanded", { panelTag: this.panelInfo.tag, expanded: this.panelInfo.expanded }));
    };
  }
  static get styles() {
    return [
      J(Pc),
      J(Tc),
      wl`
        * {
          box-sizing: border-box;
        }

        :host {
          flex: none;
          --min-width: 160px;
          --header-height: 36px;
          --content-width: var(--content-width, 100%);
          --floating-border-width: 1px;
          --floating-offset-resize-threshold: 8px;
          cursor: var(--cursor, var(--resize-cursor, default));
          overflow: hidden;
        }

        :host(:not([expanded])) {
          grid-template-rows: auto 0fr;
        }

        [part='header'] {
          align-items: center;
          color: var(--color-high-contrast);
          display: flex;
          flex: none;
          font: var(--font-small-medium);
          gap: var(--space-50);
          justify-content: space-between;
          min-width: 100%;
          padding: var(--space-50);
          user-select: none;
          -webkit-user-select: none;
          width: var(--min-width);
        }

        :host([floating]) {
          --content-height: calc(var(--section-height));
        }

        :host([floating]:not([expanded])) [part='header'] {
          --min-width: unset;
        }

        :host([floating]) [part='header'] {
          transition: border-color var(--duration-2);
        }

        :host([floating]:not([expanded])) [part='header'] {
          border-color: transparent;
        }

        [part='title'] {
          flex: auto;
          margin: 0;
          overflow: hidden;
          text-overflow: ellipsis;
        }

        [part='title']:first-child {
          margin-inline-start: var(--space-100);
        }

        [part='content'] {
          height: calc(var(--content-height) - var(--header-height));
          overflow: auto;
          transition:
            height var(--duration-2),
            width var(--duration-2),
            opacity var(--duration-2),
            visibility calc(var(--duration-2) * 2);
        }

        :host([floating]) [part='drawer-resize'] {
          display: none;
        }

        :host(:not([expanded])) [part='drawer-resize'] {
          display: none;
        }

        :host(:not([floating]):not(:last-child)) {
          border-bottom: 1px solid var(--divider-primary-color);
        }

        :host(:not([expanded])) [part='content'] {
          opacity: 0;
          visibility: hidden;
        }

        :host([floating]:not([expanded])) [part='content'] {
          width: 0;
          height: 0;
        }

        :host(:not([expanded])) [part='content'][style*='width'] {
          width: 0 !important;
        }

        :host([floating]) {
          position: fixed;
          min-width: 0;
          min-height: 0;
          z-index: calc(var(--z-index-floating-panel) + var(--z-index-focus, 0));
          top: clamp(0px, var(--top), calc(100vh - var(--section-height, var(--header-height)) * 0.5));
          left: clamp(calc(var(--section-width) * -0.5), var(--left), calc(100vw - var(--section-width) * 0.5));
          bottom: clamp(
            calc(var(--section-height, var(--header-height)) * -0.5),
            var(--bottom),
            calc(100vh - var(--section-height, var(--header-height)) * 0.5)
          );
          right: clamp(calc(var(--section-width) * -0.5), var(--right), calc(100vw - var(--section-width) * 0.5));
          width: var(--section-width);
          overflow: visible;
        }
        :host([floating]) [part='container'] {
          background: var(--background-color);
          border: var(--floating-border-width) solid var(--surface-border-color);
          -webkit-backdrop-filter: var(--surface-backdrop-filter);
          backdrop-filter: var(--surface-backdrop-filter);
          border-radius: var(--radius-2);
          margin: auto;
          box-shadow: var(--surface-box-shadow-2);
        }
        [part='container'] {
          overflow: hidden;
        }
        :host([floating][expanded]) [part='container'] {
          height: calc(100% - var(--floating-offset-resize-threshold) / 2);
          width: calc(100% - var(--floating-offset-resize-threshold) / 2);
        }

        :host([floating]:not([expanded])) {
          width: unset;
        }

        :host([floating]) .drag-handle {
          cursor: var(--resize-cursor, move);
        }

        :host([floating][expanded]) [part='content'] {
          min-width: var(--min-width);
          min-height: 0;
          width: var(--content-width);
        }

        /* :hover for Firefox, :active for others */

        :host([floating][expanded]) [part='content']:is(:hover, :active) {
          transition: none;
        }

        [part='title-button'] {
          font: var(--font-xxsmall-bold);
          padding: 0;
          text-align: start;
          text-transform: uppercase;
        }

        @media not (prefers-reduced-motion) {
          [part='toggle-button'] svg {
            transition: transform 0.15s cubic-bezier(0.2, 0, 0, 1);
          }
        }

        [part='toggle-button'][aria-expanded='true'] svg {
          transform: rotate(90deg);
        }

        .actions {
          display: none;
        }

        :host([expanded]) .actions {
          display: contents;
        }

        ::slotted(*) {
          box-sizing: border-box;
          display: block;
          /* padding: var(--space-150); */
          width: 100%;
        }

        /*workaround for outline to have a explicit height while floating by default. 
          may be removed after https://github.com/vaadin/web-components/issues/7620 is solved
        */
        :host([floating][expanded][paneltag='copilot-outline-panel']) {
          --grid-default-height: 400px;
        }

        :host([dragging]) {
          opacity: 0.4;
        }

        :host([dragging]) [part='content'] {
          pointer-events: none;
        }

        :host([hiding-while-drag-and-drop]) {
          display: none;
        }

        // dragging in drawer

        :host(:not([floating])) .drag-handle {
          cursor: grab;
        }

        :host(:not([floating])[dragging]) .drag-handle {
          cursor: grabbing;
        }
      `
    ];
  }
  connectedCallback() {
    super.connectedCallback(), this.setAttribute("role", "region"), this.reaction(
      () => se$1.getAttentionRequiredPanelConfiguration(),
      () => {
        const e2 = se$1.getAttentionRequiredPanelConfiguration();
        this.toggleAttribute(au, e2?.tag === this.panelTag && e2?.floating);
      }
    ), this.addEventListener("mouseenter", this.sectionPanelMouseEnterListener), this.reaction(
      () => g$1.operationInProgress,
      () => {
        requestAnimationFrame(() => {
          this.toggleAttribute(
            "hiding-while-drag-and-drop",
            g$1.operationInProgress === xu.DragAndDrop && this.panelInfo?.floating && !this.panelInfo.showWhileDragging
          );
        });
      }
    ), this.reaction(
      () => se$1.floatingPanelsZIndexOrder,
      () => {
        this.style.setProperty("--z-index-focus", `${se$1.getFloatingPanelZIndex(this.panelTag)}`);
      },
      { fireImmediately: true }
    ), this.addEventListener("transitionend", this.transitionEndEventListener), this.addEventListener("mousemove", this.floatingResizeHandlerMouseMoveListener), this.addEventListener("mousedown", this.floatingResizingMouseDownListener), this.addEventListener("mouseleave", this.floatingResizingMouseLeaveListener), document.addEventListener("mousemove", this.floatingResizingMouseMoveListener), document.addEventListener("mouseup", this.floatingResizingMouseUpListener);
  }
  disconnectedCallback() {
    super.disconnectedCallback(), this.removeEventListener("mouseenter", this.sectionPanelMouseEnterListener), this.removeEventListener("mousemove", this.floatingResizeHandlerMouseMoveListener), this.removeEventListener("mousedown", this.floatingResizingMouseDownListener), document.removeEventListener("mousemove", this.floatingResizingMouseMoveListener), document.removeEventListener("mouseup", this.floatingResizingMouseUpListener);
  }
  setResizePosition(e2, t, i) {
    const n = this.rectangleBeforeResizing, o2 = 0, s = window.innerWidth, a = 0, h2 = window.innerHeight, m = Math.max(o2, Math.min(s, t)), v = Math.max(a, Math.min(h2, i));
    if (e2 === "left")
      this.setFloatingResizeDirectionProps(
        "left",
        m,
        n.left - m + n.width
      );
    else if (e2 === "right")
      this.setFloatingResizeDirectionProps(
        "right",
        m,
        m - n.right + n.width
      );
    else if (e2 === "top") {
      const x = n.top - v + n.height;
      this.setFloatingResizeDirectionProps("top", v, void 0, x);
    } else if (e2 === "bottom") {
      const x = v - n.bottom + n.height;
      this.setFloatingResizeDirectionProps("bottom", v, void 0, x);
    }
  }
  willUpdate(e2) {
    super.willUpdate(e2), e2.has("panelTag") && (this.panelInfo = se$1.getPanelByTag(this.panelTag), this.setAttribute("aria-labelledby", this.panelInfo.tag.concat("-title"))), this.toggleAttribute("floating", this.panelInfo?.floating);
  }
  updated(e2) {
    super.updated(e2), this.setCssSizePositionProperties();
  }
  firstUpdated(e2) {
    super.firstUpdated(e2), document.addEventListener("mouseup", this.documentMouseUpEventListener), this.headerDraggableArea.addEventListener("mousedown", this.panelHeaderMouseDownEventListener), this.headerDraggableArea.addEventListener("mouseup", this.panelHeaderMouseUpEventListener), this.toggleAttribute("expanded", this.panelInfo?.expanded), this.toggleAttribute("individual", this.panelInfo?.individual ?? false), $u(this), this.setCssSizePositionProperties(), this.contentArea.addEventListener("mousedown", this.contentAreaMouseDownListener), u(this.shadowRoot);
  }
  render() {
    return this.panelInfo ? Ue$1`
      <div part="container">
        <div part="header" class="drag-handle">
          ${this.panelInfo.expandable !== false ? Ue$1` <button
                @mousedown="${(e2) => e2.stopPropagation()}"
                @click="${(e2) => this.toggleExpand(e2)}"
                aria-controls="content"
                aria-expanded="${this.panelInfo.expanded}"
                aria-label="Expand ${this.panelInfo.header}"
                class="icon"
                part="toggle-button">
                <span>${o.chevronRight}</span>
              </button>` : O$1}
          <h2 id="${this.panelInfo.tag}-title" part="title">
            <button
              part="title-button"
              @dblclick="${(e2) => {
      this.toggleExpand(e2), this.startDraggingDebounce.clear();
    }}">
              ${se$1.getPanelHeader(this.panelInfo)}
            </button>
          </h2>
          <div class="actions" @mousedown="${(e2) => e2.stopPropagation()}">${this.renderActions()}</div>
          ${this.renderHelpButton()} ${this.renderPopupButton()}
        </div>
        <div part="content" id="content">
          <slot name="content"></slot>
        </div>
      </div>
    ` : O$1;
  }
  getPopupButtonIcon() {
    return this.panelInfo ? this.panelInfo.panel === void 0 ? o.x : this.panelInfo.floating ? this.panelInfo.panel === "bottom" ? o.layoutBottom : this.panelInfo.panel === "left" ? o.layoutLeft : this.panelInfo.panel === "right" ? o.layoutRight : O$1 : o.share : O$1;
  }
  renderHelpButton() {
    return this.panelInfo?.helpUrl ? Ue$1` <button
      @click="${() => window.open(this.panelInfo.helpUrl, "_blank")}"
      @mousedown="${(e2) => e2.stopPropagation()}"
      aria-label="More information about ${this.panelInfo.header}"
      class="icon"
      title="More information about ${this.panelInfo.header}">
      <span>${o.help}</span>
    </button>` : O$1;
  }
  renderActions() {
    if (!this.panelInfo?.actionsTag)
      return O$1;
    const e2 = this.panelInfo.actionsTag;
    return Yl(`<${e2}></${e2}>`);
  }
  changeDockingPanel(e2) {
    const t = e2.detail.value.panel;
    if (this.panelInfo?.panel !== t) {
      const i = se$1.panels.filter((n) => n.panel === t).map((n) => n.panelOrder).sort((n, o2) => o2 - n)[0];
      Pu(this), se$1.updatePanel(this.panelInfo.tag, { panel: t, panelOrder: i + 1 });
    }
    this.panelInfo.floating && this.changePanelFloating(e2);
  }
  getResizeDirections() {
    const e2 = this.getAttribute(b);
    return e2 ? e2.split(" ") : [];
  }
};
H([
  h()
], D.prototype, "panelTag", 2);
H([
  h$1(".drag-handle")
], D.prototype, "headerDraggableArea", 2);
H([
  h$1("#content")
], D.prototype, "contentArea", 2);
H([
  h$1('[part="container"]')
], D.prototype, "container", 2);
H([
  g()
], D.prototype, "dockingItems", 2);
D = H([
  yl("copilot-section-panel-wrapper")
], D);
const oe = window.Vaadin.copilot.customComponentHandler;
if (!oe)
  throw new Error("Tried to access custom component handler before it was initialized.");
function _t(e2) {
  g$1.setOperationWaitsHmrUpdate(e2, 3e4);
}
y.on("undoRedo", (e2) => {
  const i = { files: Tt(e2), uiId: uu() }, n = e2.detail.undo ? "copilot-plugin-undo" : "copilot-plugin-redo", o2 = e2.detail.undo ? "undo" : "redo";
  _t$1(o2), _t(xu.RedoUndo), y.send(n, i);
});
function Tt(e2) {
  const t = oe.getActiveDrillDownContext();
  if (t) {
    const i = oe.getCustomComponentInfo(t);
    if (i)
      return new Array(i.customComponentFilePath);
  }
  return e2.detail.files ?? ou();
}
var Ht = (e2, t, i, n) => {
  for (var o2 = t, s = e2.length - 1, a; s >= 0; s--)
    (a = e2[s]) && (o2 = a(o2) || o2);
  return o2;
};
let Pe = class extends ql {
  static get styles() {
    return [
      J(Pc),
      J(Tc),
      J(Ic),
      wl`
        :host {
          --lumo-secondary-text-color: var(--dev-tools-text-color);
          --lumo-contrast-80pct: var(--dev-tools-text-color-emphasis);
          --lumo-contrast-60pct: var(--dev-tools-text-color-secondary);
          --lumo-font-size-m: 14px;

          position: fixed;
          bottom: 2.5rem;
          right: 0rem;
          visibility: visible; /* Always show, even if copilot is off */
          user-select: none;
          z-index: 10000;

          --dev-tools-text-color: rgba(255, 255, 255, 0.8);

          --dev-tools-text-color-secondary: rgba(255, 255, 255, 0.65);
          --dev-tools-text-color-emphasis: rgba(255, 255, 255, 0.95);
          --dev-tools-text-color-active: rgba(255, 255, 255, 1);

          --dev-tools-background-color-inactive: rgba(45, 45, 45, 0.25);
          --dev-tools-background-color-active: rgba(45, 45, 45, 0.98);
          --dev-tools-background-color-active-blurred: rgba(45, 45, 45, 0.85);

          --dev-tools-border-radius: 0.5rem;
          --dev-tools-box-shadow: 0 0 0 1px rgba(255, 255, 255, 0.05), 0 4px 12px -2px rgba(0, 0, 0, 0.4);

          --dev-tools-blue-hsl: 206, 100%, 70%;
          --dev-tools-blue-color: hsl(var(--dev-tools-blue-hsl));
          --dev-tools-green-hsl: 145, 80%, 42%;
          --dev-tools-green-color: hsl(var(--dev-tools-green-hsl));
          --dev-tools-grey-hsl: 0, 0%, 50%;
          --dev-tools-grey-color: hsl(var(--dev-tools-grey-hsl));
          --dev-tools-yellow-hsl: 38, 98%, 64%;
          --dev-tools-yellow-color: hsl(var(--dev-tools-yellow-hsl));
          --dev-tools-red-hsl: 355, 100%, 68%;
          --dev-tools-red-color: hsl(var(--dev-tools-red-hsl));

          /* Needs to be in ms, used in JavaScript as well */
          --dev-tools-transition-duration: 180ms;
        }

        .notification-tray {
          display: flex;
          flex-direction: column-reverse;
          align-items: flex-end;
          margin: 0.5rem;
          flex: none;
        }

        @supports (backdrop-filter: blur(1px)) {
          .notification-tray div.message {
            backdrop-filter: blur(8px);
          }

          .notification-tray div.message {
            background-color: var(--dev-tools-background-color-active-blurred);
          }
        }

        .notification-tray .message {
          pointer-events: auto;
          background-color: var(--dev-tools-background-color-active);
          color: var(--dev-tools-text-color);
          max-width: 40rem;
          box-sizing: border-box;
          border-radius: var(--dev-tools-border-radius);
          margin-top: 0.5rem;
          transition: var(--dev-tools-transition-duration);
          transform-origin: bottom right;
          animation: slideIn var(--dev-tools-transition-duration);
          box-shadow: var(--dev-tools-box-shadow);
          padding-top: 0.25rem;
          padding-bottom: 0.25rem;
        }

        .notification-tray .message.animate-out {
          animation: slideOut forwards var(--dev-tools-transition-duration);
        }

        .notification-tray .message .message-details {
          word-break: break-all;
        }

        .message.information {
          --dev-tools-notification-color: var(--dev-tools-blue-color);
        }

        .message.warning {
          --dev-tools-notification-color: var(--dev-tools-yellow-color);
        }

        .message.error {
          --dev-tools-notification-color: var(--dev-tools-red-color);
        }

        .message {
          display: flex;
          padding: 0.1875rem 0.75rem 0.1875rem 2rem;
          background-clip: padding-box;
        }

        .message.log {
          padding-left: 0.75rem;
        }

        .message-content {
          max-width: 100%;
          margin-right: 0.5rem;
          -webkit-user-select: text;
          -moz-user-select: text;
          user-select: text;
        }

        .message-heading {
          position: relative;
          display: flex;
          align-items: center;
          margin: 0.125rem 0;
        }

        .message .message-details {
          font-weight: 400;
          color: var(--dev-tools-text-color-secondary);
          margin: 0.25rem 0;
          display: flex;
          flex-direction: column;
        }

        .message .message-details[hidden] {
          display: none;
        }

        .message .message-details p {
          display: inline;
          margin: 0;
          margin-right: 0.375em;
          word-break: break-word;
        }

        .message .persist {
          color: var(--dev-tools-text-color-secondary);
          white-space: nowrap;
          margin: 0.375rem 0;
          display: flex;
          align-items: center;
          position: relative;
          -webkit-user-select: none;
          -moz-user-select: none;
          user-select: none;
        }

        .message .persist::before {
          content: '';
          width: 1em;
          height: 1em;
          border-radius: 0.2em;
          margin-right: 0.375em;
          background-color: rgba(255, 255, 255, 0.3);
        }

        .message .persist:hover::before {
          background-color: rgba(255, 255, 255, 0.4);
        }

        .message .persist.on::before {
          background-color: rgba(255, 255, 255, 0.9);
        }

        .message .persist.on::after {
          content: '';
          order: -1;
          position: absolute;
          width: 0.75em;
          height: 0.25em;
          border: 2px solid var(--dev-tools-background-color-active);
          border-width: 0 0 2px 2px;
          transform: translate(0.05em, -0.05em) rotate(-45deg) scale(0.8, 0.9);
        }

        .message .dismiss-message {
          font-weight: 600;
          align-self: stretch;
          display: flex;
          align-items: center;
          padding: 0 0.25rem;
          margin-left: 0.5rem;
          color: var(--dev-tools-text-color-secondary);
        }

        .message .dismiss-message:hover {
          color: var(--dev-tools-text-color);
        }

        .message.log {
          color: var(--dev-tools-text-color-secondary);
        }

        .message:not(.log) .message-heading {
          font-weight: 500;
        }

        .message.has-details .message-heading {
          color: var(--dev-tools-text-color-emphasis);
          font-weight: 600;
        }

        .message-heading::before {
          position: absolute;
          margin-left: -1.5rem;
          display: inline-block;
          text-align: center;
          font-size: 0.875em;
          font-weight: 600;
          line-height: calc(1.25em - 2px);
          width: 14px;
          height: 14px;
          box-sizing: border-box;
          border: 1px solid transparent;
          border-radius: 50%;
        }

        .message.information .message-heading::before {
          content: 'i';
          border-color: currentColor;
          color: var(--dev-tools-notification-color);
        }

        .message.warning .message-heading::before,
        .message.error .message-heading::before {
          content: '!';
          color: var(--dev-tools-background-color-active);
          background-color: var(--dev-tools-notification-color);
        }

        .ahreflike {
          font-weight: 500;
          color: var(--dev-tools-text-color-secondary);
          text-decoration: underline;
          cursor: pointer;
        }

        @keyframes slideIn {
          from {
            transform: translateX(100%);
            opacity: 0;
          }
          to {
            transform: translateX(0%);
            opacity: 1;
          }
        }

        @keyframes slideOut {
          from {
            transform: translateX(0%);
            opacity: 1;
          }
          to {
            transform: translateX(100%);
            opacity: 0;
          }
        }

        @keyframes fade-in {
          0% {
            opacity: 0;
          }
        }

        @keyframes bounce {
          0% {
            transform: scale(0.8);
          }
          50% {
            transform: scale(1.5);
            background-color: hsla(var(--dev-tools-red-hsl), 1);
          }
          100% {
            transform: scale(1);
          }
        }
      `
    ];
  }
  render() {
    return Ue$1`<div class="notification-tray">
      ${g$1.notifications.map((e2) => this.renderNotification(e2))}
    </div>`;
  }
  renderNotification(e2) {
    return Ue$1`
      <div
        class="message ${e2.type} ${e2.animatingOut ? "animate-out" : ""} ${e2.details || e2.link ? "has-details" : ""}"
        data-testid="message">
        <div class="message-content">
          <div class="message-heading">${e2.message}</div>
          <div class="message-details" ?hidden="${!e2.details && !e2.link}">
            ${hu(e2.details)}
            ${e2.link ? Ue$1`<a class="ahreflike" href="${e2.link}" target="_blank">Learn more</a>` : ""}
          </div>
          ${e2.dismissId ? Ue$1`<div
                class="persist ${e2.dontShowAgain ? "on" : "off"}"
                @click=${() => {
      this.toggleDontShowAgain(e2);
    }}>
                ${Ut(e2)}
              </div>` : ""}
        </div>
        <div
          class="dismiss-message"
          @click=${(t) => {
      yo(e2), t.stopPropagation();
    }}>
          Dismiss
        </div>
      </div>
    `;
  }
  toggleDontShowAgain(e2) {
    e2.dontShowAgain = !e2.dontShowAgain, this.requestUpdate();
  }
};
Pe = Ht([
  yl("copilot-notifications-container")
], Pe);
function Ut(e2) {
  return e2.dontShowAgainMessage ? e2.dontShowAgainMessage : "Do not show this again";
}
Eo({
  type: Ve.WARNING,
  message: "Development Mode",
  details: "This application is running in development mode.",
  dismissId: "devmode"
});
const pe = Uc(async () => {
  await Wc();
});
y.on("vite-after-update", () => {
  pe();
});
const Ae = window?.Vaadin?.connectionState?.stateChangeListeners;
Ae ? Ae.add((e2, t) => {
  e2 === "loading" && t === "connected" && g$1.active && pe();
}) : console.warn("Unable to add listener for connection state changes");
y.on("copilot-plugin-state", (e2) => {
  g$1.setIdePluginState(e2.detail), e2.preventDefault();
});
y.on("copilot-early-project-state", (e$1) => {
  e.setSpringSecurityEnabled(e$1.detail.springSecurityEnabled), e.setSpringJpaDataEnabled(e$1.detail.springJpaDataEnabled), e.setSupportsHilla(e$1.detail.supportsHilla), e.setUrlPrefix(e$1.detail.urlPrefix), e$1.preventDefault();
});
y.on("location-changed", (e2) => {
  pe();
});
y.on("copilot-ide-notification", (e2) => {
  Eo({
    type: Ve[e2.detail.type],
    message: e2.detail.message,
    dismissId: e2.detail.dismissId
  }), e2.preventDefault();
});
/**
 * @license
 * Copyright (c) 2017 The Polymer Project Authors. All rights reserved.
 * This code may only be used under the BSD style license found at http://polymer.github.io/LICENSE.txt
 * The complete set of authors may be found at http://polymer.github.io/AUTHORS.txt
 * The complete set of contributors may be found at http://polymer.github.io/CONTRIBUTORS.txt
 * Code distributed by Google as part of the polymer project is also
 * subject to an additional IP rights grant found at http://polymer.github.io/PATENTS.txt
 */
let Ie = 0, Oe = 0;
const z = [];
let se = false;
function jt() {
  se = false;
  const e2 = z.length;
  for (let t = 0; t < e2; t++) {
    const i = z[t];
    if (i)
      try {
        i();
      } catch (n) {
        setTimeout(() => {
          throw n;
        });
      }
  }
  z.splice(0, e2), Oe += e2;
}
const Bt = {
  /**
   * Enqueues a function called at microtask timing.
   *
   * @memberof microTask
   * @param {!Function=} callback Callback to run
   * @return {number} Handle used for canceling task
   */
  run(e2) {
    se || (se = true, queueMicrotask(() => jt())), z.push(e2);
    const t = Ie;
    return Ie += 1, t;
  },
  /**
   * Cancels a previously enqueued `microTask` callback.
   *
   * @memberof microTask
   * @param {number} handle Handle returned from `run` of callback to cancel
   * @return {void}
   */
  cancel(e2) {
    const t = e2 - Oe;
    if (t >= 0) {
      if (!z[t])
        throw new Error(`invalid async handle: ${e2}`);
      z[t] = null;
    }
  }
};
/**
@license
Copyright (c) 2017 The Polymer Project Authors. All rights reserved.
This code may only be used under the BSD style license found at http://polymer.github.io/LICENSE.txt
The complete set of authors may be found at http://polymer.github.io/AUTHORS.txt
The complete set of contributors may be found at http://polymer.github.io/CONTRIBUTORS.txt
Code distributed by Google as part of the polymer project is also
subject to an additional IP rights grant found at http://polymer.github.io/PATENTS.txt
*/
const Ce = /* @__PURE__ */ new Set();
class F {
  /**
   * Creates a debouncer if no debouncer is passed as a parameter
   * or it cancels an active debouncer otherwise. The following
   * example shows how a debouncer can be called multiple times within a
   * microtask and "debounced" such that the provided callback function is
   * called once. Add this method to a custom element:
   *
   * ```js
   * import {microTask} from '@vaadin/component-base/src/async.js';
   * import {Debouncer} from '@vaadin/component-base/src/debounce.js';
   * // ...
   *
   * _debounceWork() {
   *   this._debounceJob = Debouncer.debounce(this._debounceJob,
   *       microTask, () => this._doWork());
   * }
   * ```
   *
   * If the `_debounceWork` method is called multiple times within the same
   * microtask, the `_doWork` function will be called only once at the next
   * microtask checkpoint.
   *
   * Note: In testing it is often convenient to avoid asynchrony. To accomplish
   * this with a debouncer, you can use `enqueueDebouncer` and
   * `flush`. For example, extend the above example by adding
   * `enqueueDebouncer(this._debounceJob)` at the end of the
   * `_debounceWork` method. Then in a test, call `flush` to ensure
   * the debouncer has completed.
   *
   * @param {Debouncer?} debouncer Debouncer object.
   * @param {!AsyncInterface} asyncModule Object with Async interface
   * @param {function()} callback Callback to run.
   * @return {!Debouncer} Returns a debouncer object.
   */
  static debounce(t, i, n) {
    return t instanceof F ? t._cancelAsync() : t = new F(), t.setConfig(i, n), t;
  }
  constructor() {
    this._asyncModule = null, this._callback = null, this._timer = null;
  }
  /**
   * Sets the scheduler; that is, a module with the Async interface,
   * a callback and optional arguments to be passed to the run function
   * from the async module.
   *
   * @param {!AsyncInterface} asyncModule Object with Async interface.
   * @param {function()} callback Callback to run.
   * @return {void}
   */
  setConfig(t, i) {
    this._asyncModule = t, this._callback = i, this._timer = this._asyncModule.run(() => {
      this._timer = null, Ce.delete(this), this._callback();
    });
  }
  /**
   * Cancels an active debouncer and returns a reference to itself.
   *
   * @return {void}
   */
  cancel() {
    this.isActive() && (this._cancelAsync(), Ce.delete(this));
  }
  /**
   * Cancels a debouncer's async callback.
   *
   * @return {void}
   */
  _cancelAsync() {
    this.isActive() && (this._asyncModule.cancel(
      /** @type {number} */
      this._timer
    ), this._timer = null);
  }
  /**
   * Flushes an active debouncer and returns a reference to itself.
   *
   * @return {void}
   */
  flush() {
    this.isActive() && (this.cancel(), this._callback());
  }
  /**
   * Returns true if the debouncer is active.
   *
   * @return {boolean} True if active.
   */
  isActive() {
    return this._timer != null;
  }
}
/**
 * @license
 * Copyright 2017 Google LLC
 * SPDX-License-Identifier: BSD-3-Clause
 */
const B = (e2, t) => {
  const i = e2._$AN;
  if (i === void 0) return false;
  for (const n of i) n._$AO?.(t, false), B(n, t);
  return true;
}, q = (e2) => {
  let t, i;
  do {
    if ((t = e2._$AM) === void 0) break;
    i = t._$AN, i.delete(e2), e2 = t;
  } while (i?.size === 0);
}, He = (e2) => {
  for (let t; t = e2._$AM; e2 = t) {
    let i = t._$AN;
    if (i === void 0) t._$AN = i = /* @__PURE__ */ new Set();
    else if (i.has(e2)) break;
    i.add(e2), Vt(t);
  }
};
function Nt(e2) {
  this._$AN !== void 0 ? (q(this), this._$AM = e2, He(this)) : this._$AM = e2;
}
function Jt(e2, t = false, i = 0) {
  const n = this._$AH, o2 = this._$AN;
  if (o2 !== void 0 && o2.size !== 0) if (t) if (Array.isArray(n)) for (let s = i; s < n.length; s++) B(n[s], false), q(n[s]);
  else n != null && (B(n, false), q(n));
  else B(this, e2);
}
const Vt = (e2) => {
  e2.type == mo.CHILD && (e2._$AP ??= Jt, e2._$AQ ??= Nt);
};
class Ft extends _o {
  constructor() {
    super(...arguments), this._$AN = void 0;
  }
  _$AT(t, i, n) {
    super._$AT(t, i, n), He(this), this.isConnected = t._$AU;
  }
  _$AO(t, i = true) {
    t !== this.isConnected && (this.isConnected = t, t ? this.reconnected?.() : this.disconnected?.()), i && (B(this, t), q(this));
  }
  setValue(t) {
    if (Cu(this._$Ct)) this._$Ct._$AI(t, this);
    else {
      const i = [...this._$Ct._$AH];
      i[this._$Ci] = t, this._$Ct._$AI(i, this, 0);
    }
  }
  disconnected() {
  }
  reconnected() {
  }
}
/**
 * @license
 * Copyright (c) 2016 - 2025 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */
const $e = Symbol("valueNotInitialized");
class qt extends Ft {
  constructor(t) {
    if (super(t), t.type !== mo.ELEMENT)
      throw new Error(`\`${this.constructor.name}\` must be bound to an element.`);
    this.previousValue = $e;
  }
  /** @override */
  render(t, i) {
    return O$1;
  }
  /** @override */
  update(t, [i, n]) {
    return this.hasChanged(n) ? (this.host = t.options && t.options.host, this.element = t.element, this.renderer = i, this.previousValue === $e ? this.addRenderer() : this.runRenderer(), this.previousValue = Array.isArray(n) ? [...n] : n, O$1) : O$1;
  }
  /** @override */
  reconnected() {
    this.addRenderer();
  }
  /** @override */
  disconnected() {
    this.removeRenderer();
  }
  /** @abstract */
  addRenderer() {
    throw new Error("The `addRenderer` method must be implemented.");
  }
  /** @abstract */
  runRenderer() {
    throw new Error("The `runRenderer` method must be implemented.");
  }
  /** @abstract */
  removeRenderer() {
    throw new Error("The `removeRenderer` method must be implemented.");
  }
  /** @protected */
  renderRenderer(t, ...i) {
    const n = this.renderer.call(this.host, ...i);
    Fl(n, t, { host: this.host });
  }
  /** @protected */
  hasChanged(t) {
    return Array.isArray(t) ? !Array.isArray(this.previousValue) || this.previousValue.length !== t.length ? true : t.some((i, n) => i !== this.previousValue[n]) : this.previousValue !== t;
  }
}
/**
 * @license
 * Copyright (c) 2017 - 2025 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */
const te = Symbol("contentUpdateDebouncer");
class ue extends qt {
  /**
   * A property to that the renderer callback will be assigned.
   *
   * @abstract
   */
  get rendererProperty() {
    throw new Error("The `rendererProperty` getter must be implemented.");
  }
  /**
   * Adds the renderer callback to the dialog.
   */
  addRenderer() {
    this.element[this.rendererProperty] = (t, i) => {
      this.renderRenderer(t, i);
    };
  }
  /**
   * Runs the renderer callback on the dialog.
   */
  runRenderer() {
    this.element[te] = F.debounce(
      this.element[te],
      Bt,
      () => {
        this.element.requestContentUpdate();
      }
    );
  }
  /**
   * Removes the renderer callback from the dialog.
   */
  removeRenderer() {
    this.element[this.rendererProperty] = null, delete this.element[te];
  }
}
class Xt extends ue {
  get rendererProperty() {
    return "renderer";
  }
}
class Yt extends ue {
  get rendererProperty() {
    return "headerRenderer";
  }
}
class Wt extends ue {
  get rendererProperty() {
    return "footerRenderer";
  }
}
const Gt = bo(Xt), Kt = bo(Yt), Zt = bo(Wt);
var Qt = Object.defineProperty, ei = Object.getOwnPropertyDescriptor, Ue = (e2, t, i, n) => {
  for (var o2 = n > 1 ? void 0 : n ? ei(t, i) : t, s = e2.length - 1, a; s >= 0; s--)
    (a = e2[s]) && (o2 = (n ? a(t, i, o2) : a(o2)) || o2);
  return n && o2 && Qt(t, i, o2), o2;
};
let ae = class extends ht$1 {
  constructor() {
    super(...arguments), this.rememberChoice = false, this.opened = false;
  }
  firstUpdated(e2) {
    super.firstUpdated(e2), u(this.renderRoot);
  }
  render() {
    return Ue$1` <vaadin-dialog
      id="ai-dialog"
      no-close-on-outside-click
      no-close-on-esc
      overlay-class="ai-dialog"
      ?opened=${this.opened}
      ${Kt(
      () => Ue$1`
          <h2>This Operation Uses AI</h2>
          ${o.starsAlt}
        `
    )}
      ${Gt(
      () => Ue$1`
          <p>AI is a third-party service that will receive some of your project code as context for the operation.</p>
          <label>
            <input
              type="checkbox"
              @change=${(e2) => {
        this.rememberChoice = e2.target.checked;
      }} />Don’t ask again
          </label>
        `
    )}
      ${Zt(
      () => Ue$1`
          <button @click=${() => this.sendEvent("cancel")}>Cancel</button>
          <button class="primary" @click=${() => this.sendEvent("ok")}>OK</button>
        `
    )}></vaadin-dialog>`;
  }
  sendEvent(e2) {
    this.dispatchEvent(
      new CustomEvent("ai-usage-response", {
        detail: { response: e2, rememberChoice: this.rememberChoice }
      })
    );
  }
};
Ue([
  h()
], ae.prototype, "opened", 2);
ae = Ue([
  yl("copilot-ai-usage-confirmation-dialog")
], ae);
y.on("copilot-java-after-update", (e2) => {
  const t = e2.detail.classes.filter((n) => n.redefined).map((n) => n.class).join(", ");
  if (t.length === 0)
    return;
  Eo({
    type: Ve.INFORMATION,
    message: `Java changes were hot deployed for ${fu(t)}`,
    dismissId: "java-hot-deploy",
    delay: 5e3
  });
});
