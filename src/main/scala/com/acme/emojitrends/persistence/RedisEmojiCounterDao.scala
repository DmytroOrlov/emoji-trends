package com.acme.emojitrends.persistence

import javax.inject.Inject

import com.redis.RedisClientPool

class RedisEmojiCounterDao @Inject()(pool: RedisClientPool) {
  private def emojiKey(emoji: String) = s"emoji:$emoji"

  def incrementTweets: Option[Long] =
    pool.withClient(_.incr("tweets"))

  def incrementEmoji(emoji: String): Option[Long] =
    pool.withClient(_.incr(emojiKey(emoji)))

  def occurrences(emoji: String): Int =
    pool.withClient { rc =>
      rc.get(emojiKey(emoji))
        .map(_.toInt)
        .getOrElse(0)
    }

  def tweets: Int =
    pool.withClient { rc =>
      rc.get("tweets")
        .map(_.toInt)
        .getOrElse(0)
    }
}
