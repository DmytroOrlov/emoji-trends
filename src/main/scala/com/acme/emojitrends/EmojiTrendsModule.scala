package com.acme.emojitrends

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.stream.Materializer
import com.acme.emojitrends.guice.{AkkaModule, RedisModule, TwitterModule}
import com.acme.emojitrends.persistence.RedisEmojiCounterDao
import com.acme.emojitrends.stream.EmojiCounter
import com.google.inject.{AbstractModule, Provides, Singleton}
import com.typesafe.config.ConfigFactory
import net.codingwell.scalaguice.ScalaModule

import scala.concurrent.ExecutionContext

class EmojiTrendsModule extends AbstractModule with ScalaModule {
  private lazy val config = ConfigFactory.load

  override def configure(): Unit = {
    install(new TwitterModule(config.getConfig("twitter")))
    install(new AkkaModule(config))
    install(new RedisModule(config.getConfig("redis")))
  }

  @Provides
  @Singleton
  def emojiCounter(dao: RedisEmojiCounterDao)
                  (implicit system: ActorSystem, ec: ExecutionContext, materializer: Materializer): EmojiCounter = {
    implicit val adapter: LoggingAdapter = Logging(system, classOf[EmojiCounter])

    val counterConfig = config.getConfig("emojiCounter")
    val configuration = EmojiCounter.Configuration(counterConfig.getInt("bufferSize"))
    new EmojiCounter(dao, configuration)
  }
}
