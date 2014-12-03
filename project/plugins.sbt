resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "artifactory" at "http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases"

resolvers += "bigtoast-github" at "http://bigtoast.github.com/repo/"

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.8.3")

addSbtPlugin("com.github.bigtoast" % "sbt-thrift" % "0.6")
