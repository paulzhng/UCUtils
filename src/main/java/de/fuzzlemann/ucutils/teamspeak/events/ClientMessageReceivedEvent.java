package de.fuzzlemann.ucutils.teamspeak.events;

import de.fuzzlemann.ucutils.teamspeak.CommandResponse;
import de.fuzzlemann.ucutils.teamspeak.objects.TargetMode;

/**
 * @author Fuzzlemann
 */
@TSEvent.Name("notifytextmessage")
public class ClientMessageReceivedEvent extends TSEvent {

    private final TargetMode targetMode;
    private final String message;
    private final int invokerID;
    private final String invokerName;
    private final String invokerUniqueID;
    private final int targetID;

    public ClientMessageReceivedEvent(String input) {
        super(input);

        this.targetMode = TargetMode.byID(CommandResponse.parseInt(map.get("targetmode")));
        this.message = map.get("msg");
        this.invokerID = CommandResponse.parseInt(map.get("invokerid"));
        this.invokerName = map.get("invokername");
        this.invokerUniqueID = map.get("invokeruid");
        this.targetID = CommandResponse.parseInt(map.get("targetid"));
    }

    public TargetMode getTargetMode() {
        return targetMode;
    }

    public String getMessage() {
        return message;
    }

    public int getInvokerID() {
        return invokerID;
    }

    public String getInvokerName() {
        return invokerName;
    }

    public String getInvokerUniqueID() {
        return invokerUniqueID;
    }

    public int getTargetID() {
        return targetID;
    }
}
