organization := "com.flyobjectspace"

name := "FlyJava"

version := "2.0.4-SNAPSHOT"

javaSource in Compile := baseDirectory.value / "src"

javaSource in Test := baseDirectory.value / "test"

javacOptions ++= Seq("-source", "1.6")

libraryDependencies ++= Seq("junit" % "junit" % "4.11" % "test")

crossPaths := false

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false}

autoScalaLibrary := false

publishTo <<= version { v: String =>
      val nexus = "https://oss.sonatype.org/"
      if (v.trim.endsWith("SNAPSHOT"))
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases" at nexus + "service/local/staging/deploy/maven2")
    }

pomExtra :=
  <licenses>
    <license>
      <name>MIT License</name>
      <url>http://www.opensource.org/licenses/mit-license/</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
    <scm>
      <url>git@github.com:fly-object-space/fly-java.git</url>
      <connection>scm:git:git@github.com:fly-object-space/fly-java.git</connection>
    </scm>
    <url>http://www.flyobjectspace.com</url>
    <developers>
      <developer>
        <id>cjw</id>
        <name>Channing Walton</name>
        <email>channing [dot] walton [at] underscoreconsulting [dot] com</email>
        <organization>Underscore Consulting Ltd</organization>
      </developer>
      <developer>
        <id>nw</id>
        <name>Nigel Warren</name>
        <organization>Zink Digital Ltd</organization>
      </developer>
    </developers>
    <mailingLists>
      <mailingList>
        <name>User and Developer Discussion List</name>
        <archive>http://groups.google.com/group/flyobjectspace</archive>
        <post>flyobjectspace@googlegroups.com</post>
        <subscribe>flyobjectspace+subscribe@googlegroups.com</subscribe>
        <unsubscribe>flyobjectspace+unsubscribe@googlegroups.com</unsubscribe>
      </mailingList>
    </mailingLists>