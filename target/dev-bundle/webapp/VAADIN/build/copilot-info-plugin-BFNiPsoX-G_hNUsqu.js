import { q as ql, y as y$1, M as Mc, U as Ue, N as Nu, I as Io, _ as _t, b as Vu, g, s as se, O, H as Hc, R as Ru, c as Iu, E as Eo, V as Ve, a as yl } from "./indexhtml-X9N9p0Tk.js";
import { g as g$1 } from "./state-CBOKMTMT-DiIKswy3.js";
import { i } from "./base-panel-D-St23hK-DJY5heMW.js";
import { o } from "./icons-Bm90QnWr-IWQY1ovD.js";
import { e } from "./early-project-state-CqEloDes-CqEloDes.js";
const M = "copilot-info-panel{--dev-tools-red-color: red;--dev-tools-grey-color: gray;--dev-tools-green-color: green;position:relative}copilot-info-panel div.info-tray{display:flex;flex-direction:column;gap:10px}copilot-info-panel button.upgrade-btn{color:var(--blue-600);position:relative;height:unset}copilot-info-panel button.upgrade-btn .new-version-indicator{--indicator-size: 6px;top:0;right:calc(-1 * var(--indicator-size) / 2);width:var(--indicator-size);height:var(--indicator-size);box-sizing:border-box;border-radius:100%;position:absolute;background:var(--accent-color);animation:ping 2s cubic-bezier(0,0,.2,1) infinite}copilot-info-panel vaadin-button{margin-inline:var(--lumo-space-l)}copilot-info-panel dl{display:grid;grid-template-columns:auto auto;gap:0;margin:var(--space-100) var(--space-50);font:var(--font-xsmall)}copilot-info-panel dl>dt,copilot-info-panel dl>dd{padding:3px 10px;margin:0;white-space:nowrap;overflow:hidden;text-overflow:ellipsis}copilot-info-panel dd.live-reload-status>span{overflow:hidden;text-overflow:ellipsis;display:block;color:var(--status-color)}copilot-info-panel dd span.hidden{display:none}copilot-info-panel dd span.true{color:var(--dev-tools-green-color);font-size:large}copilot-info-panel dd span.false{color:var(--dev-tools-red-color);font-size:large}copilot-info-panel code{white-space:nowrap;-webkit-user-select:all;user-select:all}copilot-info-panel .checks{display:inline-grid;grid-template-columns:auto 1fr;gap:var(--space-50)}copilot-info-panel span.hint{font-size:var(--font-size-0);background:var(--gray-50);padding:var(--space-75);border-radius:var(--radius-2)}";
var k, E;
function _() {
  return E || (E = 1, k = function() {
    var e2 = document.getSelection();
    if (!e2.rangeCount)
      return function() {
      };
    for (var t = document.activeElement, o2 = [], l = 0; l < e2.rangeCount; l++)
      o2.push(e2.getRangeAt(l));
    switch (t.tagName.toUpperCase()) {
      // .toUpperCase handles XHTML
      case "INPUT":
      case "TEXTAREA":
        t.blur();
        break;
      default:
        t = null;
        break;
    }
    return e2.removeAllRanges(), function() {
      e2.type === "Caret" && e2.removeAllRanges(), e2.rangeCount || o2.forEach(function(n) {
        e2.addRange(n);
      }), t && t.focus();
    };
  }), k;
}
var D, $;
function G() {
  if ($) return D;
  $ = 1;
  var e2 = _(), t = {
    "text/plain": "Text",
    "text/html": "Url",
    default: "Text"
  }, o2 = "Copy to clipboard: #{key}, Enter";
  function l(r) {
    var a = (/mac os x/i.test(navigator.userAgent) ? "⌘" : "Ctrl") + "+C";
    return r.replace(/#{\s*key\s*}/g, a);
  }
  function n(r, a) {
    var s, g2, f, v, p, i2, h = false;
    a || (a = {}), s = a.debug || false;
    try {
      f = e2(), v = document.createRange(), p = document.getSelection(), i2 = document.createElement("span"), i2.textContent = r, i2.ariaHidden = "true", i2.style.all = "unset", i2.style.position = "fixed", i2.style.top = 0, i2.style.clip = "rect(0, 0, 0, 0)", i2.style.whiteSpace = "pre", i2.style.webkitUserSelect = "text", i2.style.MozUserSelect = "text", i2.style.msUserSelect = "text", i2.style.userSelect = "text", i2.addEventListener("copy", function(d) {
        if (d.stopPropagation(), a.format)
          if (d.preventDefault(), typeof d.clipboardData > "u") {
            s && console.warn("unable to use e.clipboardData"), s && console.warn("trying IE specific stuff"), window.clipboardData.clearData();
            var x = t[a.format] || t.default;
            window.clipboardData.setData(x, r);
          } else
            d.clipboardData.clearData(), d.clipboardData.setData(a.format, r);
        a.onCopy && (d.preventDefault(), a.onCopy(d.clipboardData));
      }), document.body.appendChild(i2), v.selectNodeContents(i2), p.addRange(v);
      var R = document.execCommand("copy");
      if (!R)
        throw new Error("copy command was unsuccessful");
      h = true;
    } catch (d) {
      s && console.error("unable to copy using execCommand: ", d), s && console.warn("trying IE specific stuff");
      try {
        window.clipboardData.setData(a.format || "text", r), a.onCopy && a.onCopy(window.clipboardData), h = true;
      } catch (x) {
        s && console.error("unable to copy using clipboardData: ", x), s && console.error("falling back to prompt"), g2 = l("message" in a ? a.message : o2), window.prompt(g2, r);
      }
    } finally {
      p && (typeof p.removeRange == "function" ? p.removeRange(v) : p.removeAllRanges()), i2 && document.body.removeChild(i2), f();
    }
    return h;
  }
  return D = n, D;
}
var K = G();
const Q = /* @__PURE__ */ Mc(K);
var X = Object.defineProperty, Y = Object.getOwnPropertyDescriptor, w = (e2, t, o2, l) => {
  for (var n = l > 1 ? void 0 : l ? Y(t, o2) : t, r = e2.length - 1, a; r >= 0; r--)
    (a = e2[r]) && (n = (l ? a(t, o2, n) : a(n)) || n);
  return l && n && X(t, o2, n), n;
};
let y = class extends i {
  constructor() {
    super(...arguments), this.serverInfo = [], this.clientInfo = [{ name: "Browser", version: navigator.userAgent }], this.handleServerInfoEvent = (e2) => {
      const t = JSON.parse(e2.data.info);
      this.serverInfo = t.versions, Nu().then((o2) => {
        o2 && (this.clientInfo.unshift({ name: "Vaadin Employee", version: "true", more: void 0 }), this.requestUpdate("clientInfo"));
      }), Io() === "success" && _t("hotswap-active", { value: Vu() });
    };
  }
  connectedCallback() {
    super.connectedCallback(), this.onCommand("copilot-info", this.handleServerInfoEvent), this.onEventBus("system-info-with-callback", (e2) => {
      e2.detail.callback(this.getInfoForClipboard(e2.detail.notify));
    }), this.reaction(
      () => g.idePluginState,
      () => {
        this.requestUpdate("serverInfo");
      }
    );
  }
  getIndex(e2) {
    return this.serverInfo.findIndex((t) => t.name === e2);
  }
  render() {
    const e$1 = g.newVaadinVersionState?.versions !== void 0 && g.newVaadinVersionState.versions.length > 0, t = [...this.serverInfo, ...this.clientInfo];
    let o2 = this.getIndex("Spring") + 1;
    o2 === 0 && (o2 = t.length), e.springSecurityEnabled && (t.splice(o2, 0, { name: "Spring Security", version: "true" }), o2++), e.springJpaDataEnabled && (t.splice(o2, 0, { name: "Spring Data JPA", version: "true" }), o2++);
    const l = t.find((n) => n.name === "Vaadin");
    return l && (l.more = Ue`<button
        aria-label="Upgrade vaadin version"
        class="upgrade-btn"
        id="new-vaadin-version-btn"
        @click="${(n) => {
      n.stopPropagation(), se.updatePanel("copilot-vaadin-versions", { floating: true });
    }}">
        Upgrade
        <span class="${e$1 ? "new-version-indicator" : ""}"></span>
      </button>`), Ue` <style>
        ${M}
      </style>
      <div class="info-tray">
        <dl>
          ${t.map(
      (n) => Ue`
              <dt>${n.name}</dt>
              <dd title="${n.version}" style="${n.name === "Java Hotswap" ? "white-space: normal" : ""}">
                ${this.renderValue(n.version)}
                <span class="more">${n.more}</span>
              </dd>
            `
    )}
          ${this.renderDevWorkflowSection()}
        </dl>
        ${this.renderDevelopmentWorkflowButton()}
      </div>`;
  }
  renderDevWorkflowSection() {
    const e2 = Io(), t = this.getIdePluginLabelText(g.idePluginState), o2 = this.getHotswapAgentLabelText(e2);
    return Ue`
      <dt>Java Hotswap</dt>
      <dd>${m(e2 === "success")} ${o2}</dd>
      ${Hc() !== "unsupported" ? Ue`<dt>IDE Plugin</dt>
            <dd>${m(Hc() === "success")} ${t}</dd>` : O}
    `;
  }
  renderDevelopmentWorkflowButton() {
    const e2 = Ru();
    let t = "", o$1 = null;
    return e2.status === "success" ? (t = "More details...", o$1 = o.checkCircle) : e2.status === "warning" ? (t = "Improve Development Workflow...", o$1 = o.alertTriangle) : e2.status === "error" && (t = "Fix Development Workflow...", o$1 = Ue`<span style="color: var(--lumo-error-color)">${o.alertCircle}</span>`), Ue`
      <vaadin-button
        id="development-workflow-guide"
        @click="${() => {
      Iu();
    }}">
        <span slot="prefix"> ${o$1}</span>
        ${t}</vaadin-button
      >
    `;
  }
  getHotswapAgentLabelText(e2) {
    return e2 === "success" ? "Java Hotswap is enabled" : e2 === "error" ? "Hotswap is partially enabled" : "Hotswap is not enabled";
  }
  getIdePluginLabelText(e2) {
    if (Hc() !== "success")
      return "Not installed";
    if (e2?.version) {
      let t = null;
      return e2?.ide && (e2?.ide === "intellij" ? t = "IntelliJ" : e2?.ide === "vscode" ? t = "VS Code" : e2?.ide === "eclipse" && (t = "Eclipse")), t ? `${e2?.version} ${t}` : e2?.version;
    }
    return "Not installed";
  }
  renderValue(e2) {
    return e2 === "false" ? m(false) : e2 === "true" ? m(true) : e2;
  }
  getInfoForClipboard(e2) {
    const t = this.renderRoot.querySelectorAll(".info-tray dt"), n = Array.from(t).map((r) => ({
      key: r.textContent.trim(),
      value: r.nextElementSibling.textContent.trim()
    })).filter((r) => r.key !== "Live reload").filter((r) => !r.key.startsWith("Vaadin Emplo")).map((r) => {
      const { key: a } = r;
      let { value: s } = r;
      if (a === "IDE Plugin")
        s = this.getIdePluginLabelText(g.idePluginState) ?? "false";
      else if (a === "Java Hotswap") {
        const g$12 = g.jdkInfo?.jrebel, f = Io();
        g$12 && f === "success" ? s = "JRebel is in use" : s = this.getHotswapAgentLabelText(f);
      } else a === "Vaadin" && s.indexOf(`
`) !== -1 && (s = s.substring(0, s.indexOf(`
`)));
      return `${a}: ${s}`;
    }).join(`
`);
    return e2 && Eo({
      type: Ve.INFORMATION,
      message: "Environment information copied to clipboard",
      dismissId: "versionInfoCopied"
    }), n.trim();
  }
};
w([
  g$1()
], y.prototype, "serverInfo", 2);
w([
  g$1()
], y.prototype, "clientInfo", 2);
y = w([
  yl("copilot-info-panel")
], y);
let T = class extends ql {
  createRenderRoot() {
    return this;
  }
  connectedCallback() {
    super.connectedCallback(), this.style.display = "flex";
  }
  render() {
    return Ue` <button
      @click=${() => {
      y$1.emit("system-info-with-callback", {
        callback: Q,
        notify: true
      });
    }}
      aria-label="Copy to Clipboard"
      class="icon"
      title="Copy to Clipboard">
      <span>${o.copy}</span>
    </button>`;
  }
};
T = w([
  yl("copilot-info-actions")
], T);
const Z = {
  header: "Info",
  expanded: false,
  panelOrder: 15,
  panel: "right",
  floating: false,
  tag: "copilot-info-panel",
  actionsTag: "copilot-info-actions",
  eager: true
  // Render even when collapsed as error handling depends on this
}, ee = {
  init(e2) {
    e2.addPanel(Z);
  }
};
window.Vaadin.copilot.plugins.push(ee);
function m(e2) {
  return e2 ? Ue`<span class="true">☑</span>` : Ue`<span class="false">☒</span>`;
}
export {
  T as Actions,
  y as CopilotInfoPanel
};
