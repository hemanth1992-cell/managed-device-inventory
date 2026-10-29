package com.example.manageddeviceinventory.core.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class InventoryEvent {
    public static final DateTimeFormatter UTC_FORMATTER = DateTimeFormatter.ISO_INSTANT;

    private String schemaVersion = "1.0";
    private String eventId;
    private String collectedAt;
    private String installationId;
    private AgentInfo agent;
    private DeviceInfo device;
    private List<FieldIssue> fieldIssues = new ArrayList<>();

    public static InventoryEvent createSample() {
        InventoryEvent event = new InventoryEvent();
        event.setEventId(UUID.randomUUID().toString());
        event.setCollectedAt(Instant.now().atOffset(ZoneOffset.UTC).format(UTC_FORMATTER));
        event.setInstallationId(UUID.randomUUID().toString());

        AgentInfo agent = new AgentInfo();
        agent.setVersion("1.0.0");
        agent.setPlatform("windows");
        event.setAgent(agent);

        DeviceInfo device = new DeviceInfo();
        device.setDeviceName("Example-PC");
        device.setHostname("Example-PC");
        device.setSystemType("64-bit operating system");
        device.setManufacturer("Example Manufacturer");

        Processor processor = new Processor();
        processor.setModel("Example 8-core processor");
        processor.setArchitecture("x86_64");
        processor.setLogicalProcessors(8);
        device.setProcessor(processor);

        Memory memory = new Memory();
        memory.setInstalledBytes(17179869184L);
        device.setMemory(memory);

        Storage storage = new Storage();
        storage.setName("System");
        storage.setTotalBytes(512110190592L);
        storage.setFreeBytes(256000000000L);
        device.setStorage(List.of(storage));

        Identifiers ids = new Identifiers();
        ids.setDeviceId(null);
        ids.setDeviceIdSource(null);
        ids.setProductId("EXAMPLE-PRODUCT-ID");
        ids.setSerialNumber("EXAMPLE-SERIAL");
        device.setIdentifiers(ids);

        NetworkInterface nic = new NetworkInterface();
        nic.setName("Ethernet");
        nic.setMac("02:00:00:00:00:01");
        nic.setKind("physical");
        device.setNetworkInterfaces(List.of(nic));

        OperatingSystemInfo os = new OperatingSystemInfo();
        os.setName("Windows");
        os.setEdition("Enterprise");
        os.setVersion("11");
        os.setInstalledAt(null);
        os.setBuild("example-build");
        device.setOs(os);

        Antivirus av = new Antivirus();
        av.setName("Example Antivirus");
        av.setStatus("enabled");
        device.setAntivirus(List.of(av));

        Battery battery = new Battery();
        battery.setPresent(true);
        battery.setPercent(78);
        battery.setCharging(false);
        device.setBattery(battery);

        User user = new User();
        user.setAccount("example.user");
        user.setDomain("EXAMPLE");
        user.setAdId("EXAMPLE\\example.user");
        user.setAdIdType("domain_account");
        user.setEmail(null);
        device.setUsers(List.of(user));

        device.setLocation(null);
        event.setDevice(device);

        FieldIssue issue1 = new FieldIssue();
        issue1.setField("device.identifiers.device_id");
        issue1.setReason("unavailable");
        event.getFieldIssues().add(issue1);

        FieldIssue issue2 = new FieldIssue();
        issue2.setField("device.identifiers.device_id_source");
        issue2.setReason("unavailable");
        event.getFieldIssues().add(issue2);

        FieldIssue issue3 = new FieldIssue();
        issue3.setField("device.os.installed_at");
        issue3.setReason("unavailable");
        event.getFieldIssues().add(issue3);

        FieldIssue issue4 = new FieldIssue();
        issue4.setField("device.users.email");
        issue4.setReason("unavailable");
        event.getFieldIssues().add(issue4);

        FieldIssue issue5 = new FieldIssue();
        issue5.setField("device.location");
        issue5.setReason("disabled");
        event.getFieldIssues().add(issue5);

        return event;
    }

    public static class AgentInfo {
        private String version;
        private String platform;

        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        public String getPlatform() { return platform; }
        public void setPlatform(String platform) { this.platform = platform; }
    }

    public static class DeviceInfo {
        private String deviceName;
        private String hostname;
        private Processor processor;
        private Memory memory;
        private List<Storage> storage;
        private Identifiers identifiers;
        private List<NetworkInterface> networkInterfaces;
        private String systemType;
        private OperatingSystemInfo os;
        private String manufacturer;
        private List<Antivirus> antivirus;
        private Battery battery;
        private List<User> users;
        private Location location;

        public String getDeviceName() { return deviceName; }
        public void setDeviceName(String deviceName) { this.deviceName = deviceName; }
        public String getHostname() { return hostname; }
        public void setHostname(String hostname) { this.hostname = hostname; }
        public Processor getProcessor() { return processor; }
        public void setProcessor(Processor processor) { this.processor = processor; }
        public Memory getMemory() { return memory; }
        public void setMemory(Memory memory) { this.memory = memory; }
        public List<Storage> getStorage() { return storage; }
        public void setStorage(List<Storage> storage) { this.storage = storage; }
        public Identifiers getIdentifiers() { return identifiers; }
        public void setIdentifiers(Identifiers identifiers) { this.identifiers = identifiers; }
        public List<NetworkInterface> getNetworkInterfaces() { return networkInterfaces; }
        public void setNetworkInterfaces(List<NetworkInterface> networkInterfaces) { this.networkInterfaces = networkInterfaces; }
        public String getSystemType() { return systemType; }
        public void setSystemType(String systemType) { this.systemType = systemType; }
        public OperatingSystemInfo getOs() { return os; }
        public void setOs(OperatingSystemInfo os) { this.os = os; }
        public String getManufacturer() { return manufacturer; }
        public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
        public List<Antivirus> getAntivirus() { return antivirus; }
        public void setAntivirus(List<Antivirus> antivirus) { this.antivirus = antivirus; }
        public Battery getBattery() { return battery; }
        public void setBattery(Battery battery) { this.battery = battery; }
        public List<User> getUsers() { return users; }
        public void setUsers(List<User> users) { this.users = users; }
        public Location getLocation() { return location; }
        public void setLocation(Location location) { this.location = location; }
    }

    public static class Processor {
        private String model;
        private String architecture;
        private Integer logicalProcessors;

        public String getModel() { return model; }
        public void setModel(String model) { this.model = model; }
        public String getArchitecture() { return architecture; }
        public void setArchitecture(String architecture) { this.architecture = architecture; }
        public Integer getLogicalProcessors() { return logicalProcessors; }
        public void setLogicalProcessors(Integer logicalProcessors) { this.logicalProcessors = logicalProcessors; }
    }

    public static class Memory {
        private Long installedBytes;

        @JsonProperty("installed_bytes")
        public Long getInstalledBytes() { return installedBytes; }
        @JsonProperty("installed_bytes")
        public void setInstalledBytes(Long installedBytes) { this.installedBytes = installedBytes; }
    }

    public static class Storage {
        private String name;
        private Long totalBytes;
        private Long freeBytes;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        @JsonProperty("total_bytes")
        public Long getTotalBytes() { return totalBytes; }
        @JsonProperty("total_bytes")
        public void setTotalBytes(Long totalBytes) { this.totalBytes = totalBytes; }

        @JsonProperty("free_bytes")
        public Long getFreeBytes() { return freeBytes; }
        @JsonProperty("free_bytes")
        public void setFreeBytes(Long freeBytes) { this.freeBytes = freeBytes; }
    }

    public static class Identifiers {
        private String deviceId;
        private String deviceIdSource;
        private String productId;
        private String serialNumber;

        @JsonProperty("device_id")
        public String getDeviceId() { return deviceId; }
        @JsonProperty("device_id")
        public void setDeviceId(String deviceId) { this.deviceId = deviceId; }

        @JsonProperty("device_id_source")
        public String getDeviceIdSource() { return deviceIdSource; }
        @JsonProperty("device_id_source")
        public void setDeviceIdSource(String deviceIdSource) { this.deviceIdSource = deviceIdSource; }

        @JsonProperty("product_id")
        public String getProductId() { return productId; }
        @JsonProperty("product_id")
        public void setProductId(String productId) { this.productId = productId; }

        @JsonProperty("serial_number")
        public String getSerialNumber() { return serialNumber; }
        @JsonProperty("serial_number")
        public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }
    }

    public static class NetworkInterface {
        private String name;
        private String mac;
        private String kind;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getMac() { return mac; }
        public void setMac(String mac) { this.mac = mac; }
        public String getKind() { return kind; }
        public void setKind(String kind) { this.kind = kind; }
    }

    public static class OperatingSystemInfo {
        private String name;
        private String edition;
        private String version;
        private String installedAt;
        private String build;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEdition() { return edition; }
        public void setEdition(String edition) { this.edition = edition; }
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }

        @JsonProperty("installed_at")
        public String getInstalledAt() { return installedAt; }
        @JsonProperty("installed_at")
        public void setInstalledAt(String installedAt) { this.installedAt = installedAt; }

        public String getBuild() { return build; }
        public void setBuild(String build) { this.build = build; }
    }

    public static class Antivirus {
        private String name;
        private String status;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    public static class Battery {
        private Boolean present;
        private Integer percent;
        private Boolean charging;

        public Boolean getPresent() { return present; }
        public void setPresent(Boolean present) { this.present = present; }
        public Integer getPercent() { return percent; }
        public void setPercent(Integer percent) { this.percent = percent; }
        public Boolean getCharging() { return charging; }
        public void setCharging(Boolean charging) { this.charging = charging; }
    }

    public static class User {
        private String account;
        private String domain;
        private String adId;
        private String adIdType;
        private String email;

        public String getAccount() { return account; }
        public void setAccount(String account) { this.account = account; }
        public String getDomain() { return domain; }
        public void setDomain(String domain) { this.domain = domain; }

        @JsonProperty("ad_id")
        public String getAdId() { return adId; }
        @JsonProperty("ad_id")
        public void setAdId(String adId) { this.adId = adId; }

        @JsonProperty("ad_id_type")
        public String getAdIdType() { return adIdType; }
        @JsonProperty("ad_id_type")
        public void setAdIdType(String adIdType) { this.adIdType = adIdType; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }

    public static class Location {
        private Double latitude;
        private Double longitude;
        private Double accuracyMeters;
        private String observedAt;

        public Double getLatitude() { return latitude; }
        public void setLatitude(Double latitude) { this.latitude = latitude; }
        public Double getLongitude() { return longitude; }
        public void setLongitude(Double longitude) { this.longitude = longitude; }

        @JsonProperty("accuracy_meters")
        public Double getAccuracyMeters() { return accuracyMeters; }
        @JsonProperty("accuracy_meters")
        public void setAccuracyMeters(Double accuracyMeters) { this.accuracyMeters = accuracyMeters; }

        @JsonProperty("observed_at")
        public String getObservedAt() { return observedAt; }
        @JsonProperty("observed_at")
        public void setObservedAt(String observedAt) { this.observedAt = observedAt; }
    }

    public static class FieldIssue {
        private String field;
        private String reason;

        public String getField() { return field; }
        public void setField(String field) { this.field = field; }
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
    }

    @JsonProperty("schema_version")
    public String getSchemaVersion() { return schemaVersion; }
    @JsonProperty("schema_version")
    public void setSchemaVersion(String schemaVersion) { this.schemaVersion = schemaVersion; }

    @JsonProperty("event_id")
    public String getEventId() { return eventId; }
    @JsonProperty("event_id")
    public void setEventId(String eventId) { this.eventId = eventId; }

    @JsonProperty("collected_at")
    public String getCollectedAt() { return collectedAt; }
    @JsonProperty("collected_at")
    public void setCollectedAt(String collectedAt) { this.collectedAt = collectedAt; }

    public String getInstallationId() { return installationId; }
    public void setInstallationId(String installationId) { this.installationId = installationId; }

    public AgentInfo getAgent() { return agent; }
    public void setAgent(AgentInfo agent) { this.agent = agent; }

    public DeviceInfo getDevice() { return device; }
    public void setDevice(DeviceInfo device) { this.device = device; }

    public List<FieldIssue> getFieldIssues() { return fieldIssues; }
    public void setFieldIssues(List<FieldIssue> fieldIssues) { this.fieldIssues = fieldIssues; }
}
