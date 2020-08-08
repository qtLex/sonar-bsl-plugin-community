package com.github._1c_syntax.bsl.sonar;

import com.github._1c_syntax.bsl.sonar.language.BSLLanguage;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonarsource.analyzer.commons.ExternalReportProvider;

import java.io.File;
import java.util.List;

public class GenericIssuesLoaderSensor implements Sensor {

  private final SensorContext context;
  private final IssuesLoader issueLoader;

  public GenericIssuesLoaderSensor(final SensorContext context) {

    this.context = context;
    this.issueLoader = new IssuesLoader(context);

  }

  @Override
  public void describe(SensorDescriptor descriptor) {

    descriptor.onlyOnLanguage(BSLLanguage.KEY);
    descriptor.name("Generic issues loader");

  }

  @Override
  public void execute(SensorContext context) {

    List<File> reportFiles = ExternalReportProvider.getReportFiles(
        context, BSLCommunityProperties.GENERIC_ISSUES_REPORT_PATH_KEY);

  }

}
