import { g, U as Ue, _ as _t, E as Eo, V as Ve, d as No, a as yl } from "./indexhtml-X9N9p0Tk.js";
import { i } from "./base-panel-D-St23hK-DJY5heMW.js";
import { o } from "./icons-Bm90QnWr-IWQY1ovD.js";
const b = "copilot-features-panel{padding:var(--space-100);font:var(--font-xsmall);display:grid;grid-template-columns:auto 1fr;gap:var(--space-50);height:auto}copilot-features-panel a{display:flex;align-items:center;gap:var(--space-50);white-space:nowrap}copilot-features-panel a svg{height:12px;width:12px;min-height:12px;min-width:12px}";
var $ = (e, a, t, n) => {
  for (var o2 = a, s = e.length - 1, r; s >= 0; s--)
    (r = e[s]) && (o2 = r(o2) || o2);
  return o2;
};
const l = window.Vaadin.devTools;
let p = class extends i {
  render() {
    return Ue` <style>
        ${b}
      </style>
      ${g.featureFlags.map(
      (e) => Ue`
          <copilot-toggle-button
            .title="${e.title}"
            ?checked=${e.enabled}
            @on-change=${(a) => this.toggleFeatureFlag(a, e)}>
          </copilot-toggle-button>
          <a class="ahreflike" href="${e.moreInfoLink}" title="Learn more" target="_blank"
            >learn more ${o.share}</a
          >
        `
    )}`;
  }
  toggleFeatureFlag(e, a) {
    const t = e.target.checked;
    _t("use-feature", { source: "toggle", enabled: t, id: a.id }), l.frontendConnection ? (l.frontendConnection.send("setFeature", { featureId: a.id, enabled: t }), Eo({
      type: Ve.INFORMATION,
      message: `“${a.title}” ${t ? "enabled" : "disabled"}`,
      details: a.requiresServerRestart ? "This feature requires a server restart" : void 0,
      dismissId: `feature${a.id}${t ? "Enabled" : "Disabled"}`
    }), No()) : l.log("error", `Unable to toggle feature ${a.title}: No server connection available`);
  }
};
p = $([
  yl("copilot-features-panel")
], p);
const x = {
  header: "Features",
  expanded: false,
  panelOrder: 35,
  panel: "right",
  floating: false,
  tag: "copilot-features-panel",
  helpUrl: "https://vaadin.com/docs/latest/flow/configuration/feature-flags"
}, F = {
  init(e) {
    e.addPanel(x);
  }
};
window.Vaadin.copilot.plugins.push(F);
export {
  p as CopilotFeaturesPanel
};
