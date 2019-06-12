package de.fuzzlemann.ucutils.teamspeak.commands;

import de.fuzzlemann.ucutils.teamspeak.CommandResponse;
import de.fuzzlemann.ucutils.teamspeak.objects.Client;
import de.fuzzlemann.ucutils.utils.text.TextUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Fuzzlemann
 */
public class ClientVariableCommand extends BaseCommand<ClientVariableCommand.Response> {

    public ClientVariableCommand(Client client) {
        this(client.getClientID());
    }

    public ClientVariableCommand(Client client, String... properties) {
        this(client.getClientID(), properties);
    }

    public ClientVariableCommand(int clientID) {
        this(clientID, "client_unique_identifier", "client_unique_identifier", "client_nickname", "client_input_muted", "client_output_muted", "client_outputonly_muted",
                "client_input_hardware", "client_output_hardware", "client_meta_data", "client_is_recording", "client_database_id", "client_channel_group_id", "client_servergroups",
                "client_away", "client_away_message", "client_type", "client_flag_avatar", "client_talk_power", "client_talk_request", "client_talk_request_msg", "client_description",
                "client_is_talker", "client_is_priority_speaker", "client_unread_messages", "client_nickname_phonetic", "client_needed_serverquery_view_power", "client_icon_id",
                "client_is_channel_commander", "client_country", "client_channel_group_inherited_channel_id", "client_flag_talking", "client_is_muted", "client_volume_modificator",
                "client_version", "client_platform", "client_login_name", "client_created", "client_lastconnected", "client_totalconnections", "client_month_bytes_uploaded",
                "client_month_bytes_downloaded", "client_total_bytes_uploaded", "client_total_bytes_downloaded", "client_input_deactivated");
    }

    public ClientVariableCommand(int clientID, String... properties) {
        super("clientvariable clid=" + clientID + " " + String.join(" ", properties));
    }

    public static class Response extends CommandResponse {
        private final int id;
        private final int clientID;
        private final int clientDatabaseID;
        private final String uniqueID;
        private final String name;
        private final String loginName;
        private final String phoneticNickname;

        private final int clientType;
        private final String version;
        private final String platform;
        private final String country;

        private final int iconID;
        private final String description;
        private final double volumeModificator;
        private final String flagAvatar;
        private final String metaData;

        private final List<Integer> serverGroups;
        private final int channelGroupID;
        private final int channelID;

        private final boolean inputMuted;
        private final boolean inputHardware;
        private final boolean outputHardware;
        private final boolean microphoneMuted;
        private final boolean soundMuted;
        private final boolean outputOnlyMuted;

        private final long totalBytesDownloaded;
        private final long monthlyBytesDownloaded;
        private final long totalBytesUploaded;
        private final long monthlyBytesUploaded;

        private final int totalConnections;
        private final long firstTimeConnected;
        private final long lastTimeConnected;

        private final boolean away;
        private final String awayMessage;

        private final boolean channelCommander;
        private final boolean prioritySpeaker;
        private final boolean isTalker;
        private final boolean talking;
        private final boolean recording;

        private final int serverQueryViewPower;
        private final int talkPower;
        private final boolean requestedTalkPower;
        private final String talkPowerRequestMessage;

        private final int unreadMessages;

        public Response(String rawResponse) {
            super(rawResponse);

            Map<String, String> map = getResponse();
            System.out.println(map);
            this.id = parseInt(map.get("id"));
            this.clientID = parseInt(map.get("clid"));
            this.clientDatabaseID = parseInt(map.get("client_database_id"));
            this.uniqueID = map.get("client_unique_identifier");
            this.name = map.get("client_nickname");
            this.loginName = map.get("client_login_name");
            this.phoneticNickname = map.get("client_nickname_phonetic");
            this.clientType = parseInt(map.get("client_type"));
            this.version = map.get("client_version");
            this.platform = map.get("client_platform");
            this.country = map.get("client_country");
            this.iconID = parseInt(map.get("client_icon_id"));
            this.description = map.get("client_description");
            this.volumeModificator = parseDouble(map.get("client_volume_modificator"));
            this.flagAvatar = map.get("client_flag_avatar");
            this.metaData = map.get("client_meta_data");
            String clientServerGroups = map.get("client_servergroups");
            this.serverGroups = clientServerGroups == null ? null : Arrays.stream(clientServerGroups.split(",")).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
            this.channelGroupID = parseInt(map.get("client_channel_group_id"));
            this.channelID = parseInt(map.get("client_channel_group_inherited_channel_id"));
            this.inputMuted = parseBoolean(map.get("client_input_muted"));
            this.inputHardware = parseBoolean(map.get("client_input_hardware"));
            this.outputHardware = parseBoolean(map.get("client_output_hardware"));
            this.microphoneMuted = parseBoolean(map.get("client_is_muted"));
            this.soundMuted = parseBoolean(map.get("client_output_muted"));
            this.outputOnlyMuted = parseBoolean(map.get("client_outputonly_muted"));
            this.totalBytesDownloaded = parseLong(map.get("client_total_bytes_downloaded"));
            this.monthlyBytesDownloaded = parseLong(map.get("client_month_bytes_downloaded"));
            this.totalBytesUploaded = parseLong(map.get("client_total_bytes_uploaded"));
            this.monthlyBytesUploaded = parseLong(map.get("client_month_bytes_uploaded"));
            this.totalConnections = parseInt(map.get("client_totalconnections"));
            this.firstTimeConnected = parseLong(map.get("client_created")) * 1000;
            this.lastTimeConnected = parseLong(map.get("client_lastconnected")) * 1000;
            this.away = parseBoolean(map.get("client_away"));
            this.awayMessage = map.get("client_away_message");
            this.channelCommander = parseBoolean(map.get("client_is_channel_commander"));
            this.prioritySpeaker = parseBoolean(map.get("client_is_priority_speaker"));
            this.isTalker = parseBoolean(map.get("client_is_talker"));
            this.talking = parseBoolean(map.get("client_flag_talking"));
            this.recording = parseBoolean(map.get("client_is_recording"));
            this.serverQueryViewPower = parseInt(map.get("client_needed_serverquery_view_power"));
            this.talkPower = parseInt(map.get("client_talk_power"));
            this.requestedTalkPower = parseBoolean(map.get("client_talk_request"));
            this.talkPowerRequestMessage = map.get("client_talk_request_msg");
            this.unreadMessages = parseInt(map.get("client_unread_messages"));
        }

        public String getMinecraftName() {
            if (description == null) return null;

            return TextUtils.stripPrefix(description);
        }

        public int getID() {
            return id;
        }

        public int getClientID() {
            return clientID;
        }

        public int getClientDatabaseID() {
            return clientDatabaseID;
        }

        public String getUniqueID() {
            return uniqueID;
        }

        public String getName() {
            return name;
        }

        public String getLoginName() {
            return loginName;
        }

        public String getPhoneticNickname() {
            return phoneticNickname;
        }

        public int getClientType() {
            return clientType;
        }

        public String getVersion() {
            return version;
        }

        public String getPlatform() {
            return platform;
        }

        public String getCountry() {
            return country;
        }

        public int getIconID() {
            return iconID;
        }

        public String getDescription() {
            return description;
        }

        public double getVolumeModificator() {
            return volumeModificator;
        }

        public String getFlagAvatar() {
            return flagAvatar;
        }

        public String getMetaData() {
            return metaData;
        }

        public List<Integer> getServerGroups() {
            return serverGroups;
        }

        public int getChannelGroupID() {
            return channelGroupID;
        }

        public int getChannelID() {
            return channelID;
        }

        public boolean isInputMuted() {
            return inputMuted;
        }

        public boolean isInputHardware() {
            return inputHardware;
        }

        public boolean isOutputHardware() {
            return outputHardware;
        }

        public boolean isMicrophoneMuted() {
            return microphoneMuted;
        }

        public boolean isSoundMuted() {
            return soundMuted;
        }

        public boolean isOutputOnlyMuted() {
            return outputOnlyMuted;
        }

        public long getTotalBytesDownloaded() {
            return totalBytesDownloaded;
        }

        public long getMonthlyBytesDownloaded() {
            return monthlyBytesDownloaded;
        }

        public long getTotalBytesUploaded() {
            return totalBytesUploaded;
        }

        public long getMonthlyBytesUploaded() {
            return monthlyBytesUploaded;
        }

        public int getTotalConnections() {
            return totalConnections;
        }

        public long getFirstTimeConnected() {
            return firstTimeConnected;
        }

        public long getLastTimeConnected() {
            return lastTimeConnected;
        }

        public boolean isAway() {
            return away;
        }

        public String getAwayMessage() {
            return awayMessage;
        }

        public boolean isChannelCommander() {
            return channelCommander;
        }

        public boolean isPrioritySpeaker() {
            return prioritySpeaker;
        }

        public boolean isTalker() {
            return isTalker;
        }

        public boolean isTalking() {
            return talking;
        }

        public boolean isRecording() {
            return recording;
        }

        public int getServerQueryViewPower() {
            return serverQueryViewPower;
        }

        public int getTalkPower() {
            return talkPower;
        }

        public boolean isRequestedTalkPower() {
            return requestedTalkPower;
        }

        public String getTalkPowerRequestMessage() {
            return talkPowerRequestMessage;
        }

        public int getUnreadMessages() {
            return unreadMessages;
        }
    }
}
