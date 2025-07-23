package qaguru;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.*;

/**
 * Rough example of how automated download of iCloud photos could look.
 * Selects photos in batches of 1000 and downloads an archive for each batch.
 * <p>
 * This test is disabled because it requires manual two-factor
 * authentication and real access to iCloud Photos.
 */

public class ICloudDownloadTest {

    @Test
    void downloadPhotosInBatches() throws Exception {
        // Configure folder for downloads if needed
        Configuration.downloadsFolder = "build/downloads";

        // Open the iCloud Photos page
        open("https://www.icloud.com/photos/");

        // Login using credentials from system properties
        String login = System.getProperty("icloudLogin");
        String password = System.getProperty("icloudPassword");

        // The selectors here are illustrative; actual selectors may differ
        $("#account_name_text_field").setValue(login);
        $("#sign-in").click();
        $("#password_text_field").setValue(password);
        $("#sign-in").click();

        // Wait for user to enter the 2FA code manually
        Thread.sleep(120_000); // 2 minutes should be enough

        // After successful login navigate to Photos application
        // and wait until the grid with photos is visible
        $("div.photos-grid").shouldBe(Condition.visible, java.time.Duration.ofSeconds(30));

        ElementsCollection photos = $$("div.photo-item");
        int total = photos.size();

        for (int start = 0; start < total; start += 1000) {
            photos = $$("div.photo-item");
            List<SelenideElement> batch = photos.stream()
                    .skip(start)
                    .limit(1000)
                    .collect(Collectors.toList());
            batch.forEach(SelenideElement::click);
            SelenideElement downloadButton = $("button.download");
            File archive = downloadButton.download();
            // Optionally wait for the file to appear
            while (!archive.exists()) {
                Thread.sleep(1000);
            }
        }
    }
}
