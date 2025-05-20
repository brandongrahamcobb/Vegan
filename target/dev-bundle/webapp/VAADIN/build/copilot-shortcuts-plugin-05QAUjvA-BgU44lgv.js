import { S as Su, a as yl, y, O, U as Ue, Y as Yl, j as ju } from "./indexhtml-X9N9p0Tk.js";
import { i } from "./base-panel-D-St23hK-DJY5heMW.js";
import { o } from "./icons-Bm90QnWr-IWQY1ovD.js";
const v = 'copilot-shortcuts-panel{display:flex;flex-direction:column;padding:var(--space-150)}copilot-shortcuts-panel h3{font:var(--font-xsmall-semibold);margin-bottom:var(--space-100);margin-top:0}copilot-shortcuts-panel h3:not(:first-of-type){margin-top:var(--space-200)}copilot-shortcuts-panel ul{display:flex;flex-direction:column;list-style:none;margin:0;padding:0}copilot-shortcuts-panel ul li{display:flex;align-items:center;gap:var(--space-50);position:relative}copilot-shortcuts-panel ul li:not(:last-of-type):before{border-bottom:1px dashed var(--border-color);content:"";inset:auto 0 0 calc(var(--size-m) + var(--space-50));position:absolute}copilot-shortcuts-panel ul li span:has(svg){align-items:center;display:flex;height:var(--size-m);justify-content:center;width:var(--size-m)}copilot-shortcuts-panel .kbds{margin-inline-start:auto}copilot-shortcuts-panel kbd{align-items:center;border:1px solid var(--border-color);border-radius:var(--radius-2);box-sizing:border-box;display:inline-flex;font-family:var(--font-family);font-size:var(--font-size-1);line-height:var(--line-height-1);padding:0 var(--space-50)}', u = window.Vaadin.copilot.tree;
if (!u)
  throw new Error("Tried to access copilot tree before it was initialized.");
var w = (t, l, h, p) => {
  for (var o2 = l, n = t.length - 1, r; n >= 0; n--)
    (r = t[n]) && (o2 = r(o2) || o2);
  return o2;
};
let d = class extends i {
  constructor() {
    super(), this.onTreeUpdated = () => {
      this.requestUpdate();
    };
  }
  connectedCallback() {
    super.connectedCallback(), y.on("copilot-tree-created", this.onTreeUpdated);
  }
  disconnectedCallback() {
    super.disconnectedCallback(), y.off("copilot-tree-created", this.onTreeUpdated);
  }
  render() {
    const t = u.hasFlowComponents();
    return Ue`<style>
        ${v}
      </style>
      <h3>Global</h3>
      <ul>
        <li>
          <span>${o.vaadinLogo}</span>
          <span>Copilot</span>
          ${a(ju.toggleCopilot)}
        </li>
        <li>
          <span>${o.terminal}</span>
          <span>Command window</span>
          ${a(ju.toggleCommandWindow)}
        </li>
        <li>
          <span>${o.flipBack}</span>
          <span>Undo</span>
          ${a(ju.undo)}
        </li>
        <li>
          <span>${o.flipForward}</span>
          <span>Redo</span>
          ${a(ju.redo)}
        </li>
      </ul>
      <h3>Selected component</h3>
      <ul>
        <li>
          <span>${o.fileCodeAlt}</span>
          <span>Go to source</span>
          ${a(ju.goToSource)}
        </li>
        ${t ? Ue`<li>
              <span>${o.code}</span>
              <span>Go to attach source</span>
              ${a(ju.goToAttachSource)}
            </li>` : O}
        <li>
          <span>${o.copy}</span>
          <span>Copy</span>
          ${a(ju.copy)}
        </li>
        <li>
          <span>${o.clipboard}</span>
          <span>Paste</span>
          ${a(ju.paste)}
        </li>
        <li>
          <span>${o.copyAlt}</span>
          <span>Duplicate</span>
          ${a(ju.duplicate)}
        </li>
        <li>
          <span>${o.userUp}</span>
          <span>Select parent</span>
          ${a(ju.selectParent)}
        </li>
        <li>
          <span>${o.userLeft}</span>
          <span>Select previous sibling</span>
          ${a(ju.selectPreviousSibling)}
        </li>
        <li>
          <span>${o.userRight}</span>
          <span>Select first child / next sibling</span>
          ${a(ju.selectNextSibling)}
        </li>
        <li>
          <span>${o.trash}</span>
          <span>Delete</span>
          ${a(ju.delete)}
        </li>
        <li>
          <span>${o.zap}</span>
          <span>Quick add from palette</span>
          ${a("<kbd>A ... Z</kbd>")}
        </li>
      </ul>`;
  }
};
d = w([
  yl("copilot-shortcuts-panel")
], d);
function a(t) {
  return Ue`<span class="kbds">${Yl(t)}</span>`;
}
const x = Su({
  header: "Keyboard Shortcuts",
  tag: "copilot-shortcuts-panel",
  width: 400,
  height: 550,
  floatingPosition: {
    top: 50,
    left: 50
  }
}), C = {
  init(t) {
    t.addPanel(x);
  }
};
window.Vaadin.copilot.plugins.push(C);
