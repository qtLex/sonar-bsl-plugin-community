package com.github._1c_syntax.bsl.sonar;

import com.github._1c_syntax.bsl.sonar.language.BSLLanguageServerRuleDefinition;
import org.junit.jupiter.api.Test;
import org.sonar.api.SonarRuntime;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.rule.ActiveRules;
import org.sonar.api.batch.rule.internal.ActiveRulesBuilder;
import org.sonar.api.batch.rule.internal.NewActiveRule;
import org.sonar.api.batch.sensor.internal.DefaultSensorDescriptor;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.internal.SonarRuntimeImpl;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.utils.Version;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

class GenericIssuesLoaderSensorTest {

  private final String BASE_PATH = "src/test/resources/generic-issues-loader";
  private final File BASE_DIR = new File(BASE_PATH);
  private final String FILE_NAME = "test.bsl";

  @Test
  void describe() {
    SensorContextTester context = SensorContextTester.create(BASE_DIR);
    GenericIssuesLoaderSensor diagnosticsLoaderSensor = new GenericIssuesLoaderSensor(context);
    DefaultSensorDescriptor sensorDescriptor = new DefaultSensorDescriptor();
    diagnosticsLoaderSensor.describe(sensorDescriptor);

    assertThat(sensorDescriptor.name()).containsIgnoringCase("Generic issues loader");
  }

  @Test
  void loadSingleReport() {
    InputFile inputFile = Tools.inputFileBSL(FILE_NAME, BASE_DIR);

    SonarRuntime sonarRuntime = SonarRuntimeImpl.forSonarLint(Version.create(7, 9));
    SensorContextTester context = SensorContextTester.create(BASE_DIR);
    context.setRuntime(sonarRuntime);
    context.settings().setProperty(
        BSLCommunityProperties.GENERIC_ISSUES_REPORT_PATH_KEY,
        "bsl-generic-json.json, empty.json, does-not-exist.json"
    );
    context.fileSystem().add(inputFile);

    ActiveRules activeRules = new ActiveRulesBuilder()
      .addRule(new NewActiveRule.Builder()
          .setRuleKey(RuleKey.of(BSLLanguageServerRuleDefinition.REPOSITORY_KEY, "OneStatementPerLine"))
          .setName("OneStatementPerLine")
          .build()
      )
      .addRule(new NewActiveRule.Builder()
          .setRuleKey(RuleKey.of(BSLLanguageServerRuleDefinition.REPOSITORY_KEY, "CodeOutOfRegion"))
          .setName("CodeOutOfRegion")
          .build()
      )
      .addRule(new NewActiveRule.Builder()
          .setRuleKey(RuleKey.of(BSLLanguageServerRuleDefinition.REPOSITORY_KEY, "UnreachableCode"))
          .setName("UnreachableCode")
          .build()
      )
      .addRule(new NewActiveRule.Builder()
          .setRuleKey(RuleKey.of(BSLLanguageServerRuleDefinition.REPOSITORY_KEY, "DeprecatedMessage"))
          .setName("DeprecatedMessage")
          .build()
      )
      .addRule(new NewActiveRule.Builder()
          .setRuleKey(RuleKey.of(BSLLanguageServerRuleDefinition.REPOSITORY_KEY, "SemicolonPresence"))
          .setName("SemicolonPresence")
          .build()
      )
      .addRule(new NewActiveRule.Builder()
          .setRuleKey(RuleKey.of(BSLLanguageServerRuleDefinition.REPOSITORY_KEY, "UseLessForEach"))
          .setName("UseLessForEach")
          .build()
      )
      .build();
    context.setActiveRules(activeRules);

    GenericIssuesLoaderSensor diagnosticsLoaderSensor = new GenericIssuesLoaderSensor(context);
    diagnosticsLoaderSensor.execute(context);

    assertThat(context.isCancelled()).isFalse();
  }

}