import sbt.util
ThisBuild / organization := "com.softwarecolony"
ThisBuild / version      := "0.1//-SNAPSHOT"

name := "Flipper"

val basicSettings = Seq(
  version := "0.1",
  scalaVersion := "2.13.0",
  publishMavenStyle := true,

  testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-v"),
  crossPaths := false,

  libraryDependencies += "org.apache.pdfbox" % "pdfbox" % "2.0.5",
  libraryDependencies += "org.apache.pdfbox" % "fontbox" % "2.0.5",
  libraryDependencies += "junit" % "junit" % "4.10" % Test,
  libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test",
  libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.1" % Test withSources(),
  libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.14.3" % Test,
  libraryDependencies += "org.apache.opennlp" % "opennlp-tools" % "1.8.4",
//  libraryDependencies += "net.sourceforge.tess4j" % "tess4j" % "3.4.4",
  libraryDependencies += "net.sourceforge.tess4j" % "tess4j" % "4.5.1",

    libraryDependencies += "com.itextpdf.tool" % "xmlworker" % "5.5.10",
  libraryDependencies += "com.itextpdf" % "itextpdf" % "5.5.10",
  libraryDependencies += "com.lihaoyi" %% "scalatags" % "0.9.1",
  libraryDependencies += "org.json4s" %% "json4s-ext" % "3.6.8",
  libraryDependencies += "org.json4s" %% "json4s-native" % "3.6.8",
  libraryDependencies += "org.odftoolkit" % "odfdom-java" % "0.8.7",
  libraryDependencies += "org.languagetool" % "language-en" % "4.0",
  libraryDependencies += "org.languagetool" % "language-pt" % "4.0",
  libraryDependencies += "net.htmlparser.jericho" % "jericho-html" % "3.4",
  libraryDependencies += "org.odftoolkit" % "simple-odf" % "0.6.6",
  libraryDependencies += "net.sf.cssbox" % "pdf2dom" % "1.7",
  libraryDependencies += "com.sksamuel.scrimage" % "scrimage-core_2.12" % "3.0.0-alpha4",
  libraryDependencies += "com.sksamuel.scrimage" % "scrimage-filters_2.12" % "3.0.0-alpha4"
)

//pgpReadOnly := false

lazy val root = project.in(file(".")).aggregate(reader, generator, converter).settings(basicSettings)

lazy val reader = project.in(file("reader")).settings(basicSettings)

lazy val generator = project.in(file("generator")).settings(basicSettings)

lazy val converter = project.in(file("converter")).dependsOn(reader).settings(basicSettings)
