package com.acme.emojitrends.guice

import com.google.inject.{AbstractModule, Provides, Singleton}
import com.typesafe.config.Config
import net.codingwell.scalaguice.ScalaModule
import twitter4j.conf.ConfigurationBuilder
import twitter4j.{TwitterStream, TwitterStreamFactory}

class TwitterModule(config: Config) extends AbstractModule with ScalaModule {
  override def configure(): Unit = ()

  @Provides
  @Singleton
  def twitterStream(): TwitterStream = {
    val oAuthConf = config.getConfig("oAuth")
    val configuration = new ConfigurationBuilder()
      .setOAuthConsumerKey(oAuthConf.getString("consumerKey"))
      .setOAuthConsumerSecret(oAuthConf.getString("consumerSecret"))
      .setOAuthAccessToken(oAuthConf.getString("token"))
      .setOAuthAccessTokenSecret(oAuthConf.getString("tokenSecret"))
      .build
    new TwitterStreamFactory(configuration).getInstance()
  }
}
