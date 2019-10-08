import com.typesafe.sbt.SbtGit.git

val pubVersion = "0.11.0-RC3-my"

val publishSettings = List(
  name := "Typed Schema",
  organization := "ru.tinkoff",
  description := "Typelevel DSL for defining webservices, convertible to akka-http/finagle and swagger definitions",
  publishMavenStyle := true,
  publishTo := Some(
    if (isSnapshot.value)
      Opts.resolver.sonatypeSnapshots
    else
      Opts.resolver.sonatypeStaging
  ),
  credentials += Credentials(Path.userHome / ".sbt" / ".ossrh-credentials"),
  version := {
    val branch = git.gitCurrentBranch.value
    if (branch == "master") pubVersion
    else s"$pubVersion-$branch-SNAPSHOT"
  },
  scmInfo := Some(
    ScmInfo(
      url("https://github.com/TinkoffCreditSystems/typed-schema"),
      "git@github.com:username/projectname.git"
    )
  )
)

val doNotPublish = List(publish := {}, publishLocal := {}, PgpKeys.publishSigned := {})

licenses in ThisBuild += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0"))
publishMavenStyle in ThisBuild := true

homepage in ThisBuild := Some(url("https://github.com/TinkoffCreditSystems/typed-schema"))
developers in ThisBuild := List(
  Developer("odomontois", "Oleg Nizhnik", "odomontois@gmail.com", url("https://github.com/odomontois"))
)

val minorVersion = SettingKey[Int]("minor scala version")

val crossCompile = crossScalaVersions in ThisBuild := List("2.11.12", "2.12.9")

val commonScalacOptions = scalacOptions ++= List(
  "-deprecation",
  "-feature",
  "-language:existentials",
  "-language:experimental.macros",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:postfixOps"
)

val setExperimental = scalacOptions ++= {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, y)) if y == 11 => Seq("-Xexperimental")
    case _                       => Seq.empty[String]
  }
}

val specificScalacOptions = scalacOptions ++= {
  minorVersion.value match {
    case 11 | 12 => List("-Ypartial-unification")
    case 13      => List("-Ymacro-annotations")
  }
}

val setMinorVersion = minorVersion := {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, v)) => v.toInt
    case _            => 0
  }
}

lazy val compilerPlugins = libraryDependencies ++= List(
  compilerPlugin("org.typelevel" %% "kind-projector"     % Version.kindProjector),
  compilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1"),
)

val paradise = libraryDependencies ++= {
  minorVersion.value match {
    case 11 | 12 => List(compilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.patch))
    case 13      => List()
  }
}

val magnolia = libraryDependencies += "com.propensive" %% "magnolia" % {
  minorVersion.value match {
    case 11 => Version.magnolia211
    case _  => Version.magnolia213
  }
}

val monocle = libraryDependencies ++= List("core", "macro").map(
  module =>
    "com.github.julien-truffaut" %% s"monocle-$module" % {
      minorVersion.value match {
        case 11 | 12 => Version.monocle
        case 13      => Version.monocle213
      }
    }
)

val circe = libraryDependencies ++= List("core", "parser", "generic", "generic-extras").map(
  module =>
    "io.circe" %% s"circe-$module" % {
      minorVersion.value match {
        case 11      => Version.circe211
        case 12 | 13 => Version.circe
      }
    }
) :+ "io.circe" %% "circe-derivation" % {
  minorVersion.value match {
    case 11      => Version.circe211
    case 12 | 13 => Version.circeDerivation
  }
}

val scalatags = libraryDependencies += "com.lihaoyi" %% "scalatags" % {
  minorVersion.value match {
    case 11      => Version.scalaTags211
    case 12 | 13 => Version.scalaTags
  }
}

val akkaHttpCirce = libraryDependencies ++= {
  minorVersion.value match {
    case 11 | 12 => List("de.heikoseeberger" %% "akka-http-circe" % Version.akkaHttpCirce)
    case 13      => Nil
  }
}

val catsCore   = "org.typelevel"        %% s"cats-core"   % Version.cats
val catsFree   = "org.typelevel"        %% s"cats-free"   % Version.cats
val catsEffect = "org.typelevel"        %% s"cats-effect" % Version.catsEffect
val simulacrum = "com.github.mpilquist" %% "simulacrum"   % Version.simulacrum
val shapeless  = "com.chuusai"          %% "shapeless"    % Version.shapeless
val enumeratum = "com.beachape"         %% "enumeratum"   % Version.enumeratum

val akkaHttpLib     = "com.typesafe.akka" %% "akka-http"         % Version.akkaHttp
val scalatest       = "org.scalatest"     %% "scalatest"         % Version.scalaTest % Test
val scalacheck      = "org.scalacheck"    %% "scalacheck"        % Version.scalaCheck % Test
val akkaTestKit     = "com.typesafe.akka" %% "akka-testkit"      % Version.akka % Test
val akkaHttpTestKit = "com.typesafe.akka" %% "akka-http-testkit" % Version.akkaHttp % Test
val finagleHttp     = "com.twitter"       %% "finagle-http"      % Version.finagle
val http4sLib       = "org.http4s"        %% "http4s-core"       % Version.http4s
val scalazDeriving  = "org.scalaz"        %% "scalaz-deriving"   % Version.scalazDeriving
val scalazDMacro    = "org.scalaz"        %% "deriving-macro"    % Version.scalazDeriving
val derevo          = "org.manatki"       %% "derevo-cats"       % Version.derevo
val swaggerUILib    = "org.webjars.npm"   % "swagger-ui-dist"    % Version.swaggerUI
val scalaTags       = "com.lihaoyi"       %% "scalatags"         % Version.scalaTags
val env             = "ru.tinkoff"        %% "tofu-env"          % Version.tofu
val http4sCirce     = "org.http4s"        %% "http4s-circe"      % Version.http4s

val akka   = List("actor", "stream").map(module => "com.typesafe.akka" %% s"akka-$module" % Version.akka)
val zio    = List("dev.zio" %% "zio" % Version.zio, "dev.zio" %% "zio-interop-cats" % Version.zioCats)
val tethys = List("core", "jackson").map(module => "com.tethys-json" %% s"tethys-$module" % Version.tethys)

val reflect  = libraryDependencies += scalaOrganization.value % "scala-reflect"  % scalaVersion.value
val compiler = libraryDependencies += scalaOrganization.value % "scala-compiler" % scalaVersion.value

val enumeratumCirce = "com.beachape" %% "enumeratum-circe" % Version.enumeratumCirce

def resourcesOnCompilerCp(config: Configuration): Setting[_] =
  managedClasspath in config := {
    val res = (resourceDirectory in config).value
    val old = (managedClasspath in config).value
    Attributed.blank(res) +: old
  }

val swaggerUIVersion = SettingKey[String]("swaggerUIVersion")

lazy val testLibs = libraryDependencies ++= scalacheck :: scalatest :: Nil

lazy val commonSettings = publishSettings ++ List(
  compilerPlugins,
  commonScalacOptions,
  setExperimental,
  specificScalacOptions,
  crossCompile,
  setMinorVersion,
  testLibs
)

val compile213 = List(crossScalaVersions += "2.13.0")

lazy val kernel = project
  .in(file("modules/kernel"))
  .settings(
    commonSettings,
    compile213,
    moduleName := "typed-schema-typedsl",
    libraryDependencies ++= catsCore :: simulacrum :: shapeless :: enumeratum :: Nil
  )

lazy val param = project
  .in(file("modules/param"))
  .dependsOn(kernel)
  .settings(
    commonSettings,
    compile213,
    moduleName := "typed-schema-param",
    magnolia
  )

lazy val macros = project
  .in(file("modules/macros"))
  .dependsOn(kernel)
  .settings(
    commonSettings,
    compile213,
    moduleName := "typed-schema-macros",
    libraryDependencies ++= shapeless :: catsCore :: akkaHttpTestKit :: Nil,
    reflect
  )

lazy val swagger = project
  .in(file("modules/swagger"))
  .dependsOn(kernel, macros)
  .settings(
    commonSettings,
    compile213,
    moduleName := "typed-schema-swagger",
    libraryDependencies ++= enumeratum :: enumeratumCirce :: Nil,
    magnolia,
    monocle,
    paradise,
    circe
  )

lazy val akkaHttp = project
  .in(file("modules/akkaHttp"))
  .dependsOn(kernel, macros, param)
  .settings(
    commonSettings,
    compile213,
    moduleName := "typed-schema-akka-http",
    libraryDependencies ++= akkaHttpLib :: akka
  )

lazy val finagle = project
  .in(file("modules/finagle"))
  .dependsOn(kernel, macros, param)
  .settings(
    commonSettings,
    moduleName := "typed-schema-finagle",
    libraryDependencies ++= finagleHttp :: catsEffect :: catsFree :: Nil
  )

lazy val http4s = project
  .in(file("modules/http4s"))
  .dependsOn(kernel, macros, param)
  .settings(
    commonSettings,
    moduleName := "typed-schema-http4s",
    libraryDependencies ++= http4sLib :: http4sCirce :: catsEffect :: Nil,
    circe
  )

lazy val finagleCirce = project
  .in(file("modules/finagleCirce"))
  .dependsOn(finagle)
  .settings(
    commonSettings,
    moduleName := "typed-schema-finagle-circe",
    circe
  )

lazy val finagleTethys = project
  .in(file("modules/finagleTethys"))
  .dependsOn(finagle)
  .settings(
    commonSettings,
    moduleName := "typed-schema-finagle-tethys",
    libraryDependencies ++= tethys
  )

lazy val finagleZio = project
  .in(file("modules/finagle-zio"))
  .dependsOn(finagle)
  .settings(
    commonSettings,
    moduleName := "typed-schema-finagle-zio",
    libraryDependencies ++= catsEffect :: zio
  )

lazy val finagleCommon = project
  .in(file("modules/finagle-common"))
  .dependsOn(finagle, swagger)
  .settings(
    commonSettings,
    moduleName := "typed-schema-finagle-common"
  )

lazy val finagleEnv = project
  .in(file("modules/finagle-env"))
  .dependsOn(finagle)
  .settings(
    commonSettings,
    moduleName := "typed-schema-finagle-env",
    libraryDependencies ++= catsEffect :: env :: Nil
  )

lazy val main = project
  .in(file("modules/main"))
  .dependsOn(kernel, macros, swagger, akkaHttp)
  .settings(
    commonSettings,
    compile213,
    moduleName := "typed-schema-base",
    libraryDependencies ++= akkaHttpLib :: akkaHttpTestKit :: akkaTestKit :: akka,
    akkaHttpCirce
  )

lazy val scalaz = project
  .in(file("modules/scalaz"))
  .dependsOn(swagger, param)
  .settings(
    commonSettings,
    moduleName := "typed-schema-scalaz",
    libraryDependencies ++= scalazDeriving :: scalazDMacro :: Nil,
    addCompilerPlugin("org.scalaz" %% "deriving-plugin" % Version.scalazDeriving),
    resourcesOnCompilerCp(Compile)
  )

lazy val swaggerUI =
  (project in file("modules/swaggerUI"))
    .dependsOn(swagger)
    .enablePlugins(BuildInfoPlugin)
    .settings(
      commonSettings,
      compile213,
      moduleName := "typed-schema-swagger-ui",
      libraryDependencies ++= swaggerUILib :: Nil,
      swaggerUIVersion := {
        libraryDependencies.value
          .find(_.name == "swagger-ui-dist")
          .map(_.revision)
          .get
      },
      scalatags,
      buildInfoKeys := swaggerUIVersion :: Nil,
      buildInfoPackage := "ru.tinkoff.tschema.swagger"
    )

lazy val docs = project
  .in(file("modules/docs"))
  .enablePlugins(ScalaUnidocPlugin)
  .settings(
    doNotPublish,
    unidocProjectFilter in (ScalaUnidoc, unidoc) := inProjects(main, kernel, swagger, akkaHttp)
  )
  .dependsOn(kernel, macros, main, akkaHttp)

lazy val typedschema =
  (project in file("."))
    .dependsOn(macros, kernel, main)
    .settings(doNotPublish, publishSettings)
    .aggregate(
      macros,     //
      kernel,     //
      main,       //
      param,      //
      swagger,    //
      akkaHttp,   //
      http4s,     //
      scalaz,     //
      finagle,    //
      finagleZio, //
      finagleEnv, //
      finagleCirce,
      finagleTethys, //
      finagleCommon, //
      swaggerUI,     //
      docs
    )
