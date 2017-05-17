package util

object StringUtil {
  def normalisedEqual(s1: String, s2: String): Boolean = {
    def normalised(s: String) = s.toLowerCase.trim.replaceAll("\\s+", "")
    normalised(s1) == normalised(s2)
  }
}
