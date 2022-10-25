package votee.models

import spire.math.Rational

/**
 * Created by Abanda Ludovic on 29/03/2022.
 */

sealed trait Election[C <: Candidate, B[+CC >: C <: Candidate] <: Ballot[CC, B], W <: Winner[C]]:

  protected val DEFAULT_TIE_RESOLVER: TieResolver[C] = Election.TieResolvers.doNothingTieResolver

  /**
   * A method that resolve ties for candidates with equal scores using the given TieResolver
   *
   * @param sortedCandidateScores A sorted Seq of Candidates and their scores
   * @param tieResolver A Tie Resolver
   * @return A new Seq of candidates and their scores
   *
   * @see [[votee.models.TieResolver]]
   * @see [[Election.TieResolvers.doNothingTieResolver]]
   */
  protected final def resolveTies(sortedCandidateScores: Seq[(C, Rational)])(using tieResolver: TieResolver[C]):Seq[(C, Rational)] =
  //Todo make this tail recursive
  // Verify the tie resolver laws before using it or not (take the default tie resolver in this case)
    def partition(Seq: Seq[(C, Rational)]): Seq[(C, Rational)] =
      if Seq.isEmpty || Seq.length == 1 then Seq
      else Seq.partition(_._2 == Seq.head._2) match
        case (left, right) => tieResolver.resolve(left) ++ partition(right)
    end partition

    partition(sortedCandidateScores)
  end resolveTies



  def run(ballots: Seq[B[C]], candidates: Seq[C], vacancies: Int)(tieResolver: TieResolver[C] = DEFAULT_TIE_RESOLVER): Seq[W]
end Election

object Election:
  object TieResolvers:
    import scala.util.Random
    /**
     * A naive tie Resolver that shuffles the Seq of tied candidates.
     * While this is a fairer method of resolving ties, it is worth noting that identical elections may produce different results if some candidates have the same score.
     * @see [[votee.models.TieResolver]]
     */
    def randomTieResolver[C <: Candidate]: TieResolver[C] = (candidateScores: Seq[(C, Rational)]) => Random.shuffle(candidateScores)

    /**
     * A simple tie resolver that does nothing to the original Seq of tied candidates and simply returns it.
     * This method of resolving ties isn't fair at all. However the same result is guaranteed for identical elections
     * @see [[votee.models.TieResolver]]
     */
    def doNothingTieResolver[C <: Candidate]: TieResolver[C] = (candidateScores: Seq[(C, Rational)]) => candidateScores

    /**
     * A simple tie resolver that reverses the ordering in the original Seq of tied candidates.
     * This method of resolving ties isn't fair at all. However the same result is guaranteed for identical elections.
     * @see [[votee.models.TieResolver]]
     */
    def reverseTieResolver[C <: Candidate]: TieResolver[C] = (candidateScores: Seq[(C, Rational)]) => candidateScores.reverse
end Election

trait PreferentialElection[C <: Candidate, B[+CC >: C <: Candidate] <: Ballot[CC, B]] extends Election[C, B, Winner[C]]:

  final val MAJORITY_THRESHOLD: Rational = Rational(1,2)

  final def countFirstVotes(ballots: Seq[B[C]], candidates: Seq[C]): Map[C, Rational] =
    //We are interested only in the first valid candidate in the ballot
    ballots.flatMap(ballot => ballot.preferences.find(candidates.contains).map((_, ballot.weight))).groupMapReduce(_._1)(_._2)(_ + _)
  end countFirstVotes

  final def countLastVotes(ballots: Seq[B[C]], candidates: Seq[C]): Map[C, Rational] =
    //We are interested only in the last valid candidate in the ballot
    ballots.flatMap(ballot => ballot.preferences.findLast(candidates.contains).map((_, ballot.weight))).groupMapReduce(_._1)(_._2)(_ + _)
  end countLastVotes
  
end PreferentialElection