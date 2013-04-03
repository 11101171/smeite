import sbt._
import Keys._

import play.Project._

object ApplicationBuild extends Build {

  val appName         = "smeite"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    jdbc,
    "mysql" % "mysql-connector-java" % "5.1.21"
    ,"com.typesafe.slick" %% "slick" % "1.0.0"
    ,"org.jsoup" % "jsoup" % "1.7.1"
    ,"net.coobird" % "thumbnailator" % "0.4.3"
    //   , "com.typesafe" %% "play-plugins-mailer" % "2.1-SNAPSHOT"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here
  )
}
            
