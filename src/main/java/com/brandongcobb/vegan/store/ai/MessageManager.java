package com.brandongcobb.vegan.store.ai;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.concurrent.CompletableFuture;

public class MessageManager {

    private final Div assistantOverlay = new Div();
    private final VerticalLayout messageContainer = new VerticalLayout();
    private final TextArea inputField = new TextArea();
    private final Button sendButton = new Button("Send");

    public MessageManager() {
        assistantOverlay.getStyle()
                .set("position", "fixed")
                .set("bottom", "100px")
                .set("right", "24px")
                .set("width", "300px")
                .set("background", "rgba(255,255,255,0.9)")
                .set("box-shadow", "0 0 10px rgba(0,0,0,0.2)")
                .set("border-radius", "12px")
                .set("padding", "1em")
                .set("z-index", "9999")
                .set("display", "none")
                .set("flex-direction", "column")
                .set("gap", "0.5em");

        messageContainer.setPadding(false);
        messageContainer.setSpacing(true);

        inputField.setPlaceholder("Ask me anything...");
        inputField.setWidthFull();

        sendButton.setWidthFull();
        sendButton.addClickListener(e -> sendMessage());

        assistantOverlay.add(messageContainer, inputField, sendButton);
        UI.getCurrent().getElement().appendChild(assistantOverlay.getElement());
    }

    public void toggleVisibility() {
        boolean isVisible = "block".equals(assistantOverlay.getStyle().get("display"));
        assistantOverlay.getStyle().set("display", isVisible ? "none" : "block");
    }

    public void displayMessage(String message, boolean isUser) {
        Span msg = new Span(message);
        msg.getStyle()
                .set("background", isUser ? "#dcf8c6" : "#f1f0f0")
                .set("padding", "0.5em 1em")
                .set("border-radius", "12px")
                .set("align-self", isUser ? "flex-end" : "flex-start");

        messageContainer.add(msg);
    }

    private void sendMessage() {
        String userInput = inputField.getValue();
        if (userInput == null || userInput.isBlank()) return;

        displayMessage(userInput, true);
        inputField.clear();

        // Simulate AI processing - replace with MCP call
        CompletableFuture.runAsync(() -> {
            try { Thread.sleep(500); } catch (InterruptedException ignored) {}
            UI.getCurrent().access(() -> displayMessage("(AI response placeholder)", false));
        });
    }
}