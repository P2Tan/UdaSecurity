module Image{
    exports com.udacity.catpoint.service;
    requires java.desktop;
    requires software.amazon.awssdk.services.rekognition;
    requires software.amazon.awssdk.core;
    requires software.amazon.awssdk.regions;
    requires software.amazon.awssdk.auth;
    requires org.slf4j;
}