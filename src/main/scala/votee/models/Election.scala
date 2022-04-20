package votee.models

import votee.utils.Rational

import scala.collection.mutable

/**
 * Created by Abanda Ludovic on 29/03/2022.
 */

trait Election[C <: Candidate, B <: Ballot[C], W <: Winner[C]]:

  protected val DEFAULT_TIE_RESOLVER: TieResolver[C] = Election.TieResolvers.doNothingTieResolver

  protected final def resolveTies(candidateScores: List[(C, Rational)], tieResolver: TieResolver[C]):List[(C, Rational)] =
    val sorted = candidateScores.sortWith(_._2 > _._2)

    def partition(list: List[(C, Rational)]): List[(C, Rational)] =
      if list.isEmpty || list.length == 1 then list
      else list.partition(_._2 == list.head._2) match
        case (bigger, smaller) => tieResolver.resolve(bigger) ++ partition(smaller)
    partition(sorted)


  def run(ballots: List[B], candidates: List[C], vacancies: Int)(tieResolver: TieResolver[C] = DEFAULT_TIE_RESOLVER): List[W]
end Election

object Election:
  object TieResolvers:
    import scala.util.Random
    /**
     * A naive tie Resolver that shuffles the list tied candidates.
     * While this is a fairer method of resolving ties, it is worth noting that identical elections may produce different results if some candidates have the same score.
     */
    def randomTieResolver[C <: Candidate]: TieResolver[C] = (candidateScores: List[(C, Rational)]) => Random.shuffle(candidateScores)

    /**
     * A simple tie resolver that does nothing to the original list of tied candidates and simply returns it.
     * This method of resolving ties isn't fair at all. However the same result is guaranteed for identical elections
     */
    def doNothingTieResolver[C <: Candidate]: TieResolver[C] = (candidateScores: List[(C, Rational)]) => candidateScores

    /**
     * A simple tie resolver that reverses the ordering in the original list of tied candidates.
     * This method of resolving ties isn't fair at all. However the same result is guaranteed for identical elections
     */
    def reverseTieResolver[C <: Candidate]: TieResolver[C] = (candidateScores: List[(C, Rational)]) => candidateScores.reverse
end Election

trait PreferentialElection[C <: Candidate, B <: Ballot[C]] extends Election[C, B, Winner[C]]:
  val MAJORITY_THRESHOLD: Rational = Rational(1,2)
  def countFirstVotes(ballots: List[B], candidates: List[C]): Map[C, Rational] =
    val candidateScoreMap = new mutable.HashMap[C, Rational]

    //We are interested only in the first valid candidate in the ballot
    ballots.foreach { ballot =>
      ballot.preferences.find(candidates.contains(_)) match {
        case Some(candidate) => candidateScoreMap(candidate) = ballot.weight + candidateScoreMap.getOrElse(candidate, Rational(0))
        case _ =>
      }
    }
    Map.empty ++ candidateScoreMap
  end countFirstVotes

  def countLastVotes(ballots: List[B], candidates: List[C]): Map[C, Rational] =
    val candidateScoreMap = new mutable.HashMap[C, Rational]

    //We are interested only in the last valid candidate in the ballot
    ballots.foreach { ballot =>
      ballot.preferences.findLast(candidates.contains(_)) match {
        case Some(candidate) => candidateScoreMap(candidate) = ballot.weight + candidateScoreMap.getOrElse(candidate, Rational(0))
        case _ =>
      }
    }
    Map.empty ++ candidateScoreMap
  end countLastVotes
  
end PreferentialElection