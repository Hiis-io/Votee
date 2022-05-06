package votee.algorithms

import org.scalatest.flatspec.AnyFlatSpec
import spire.math.Rational
import votee.models.{PreferentialBallot, PreferentialCandidate}
import votee.utils.Parser

/**
 * Created by Abanda Ludovic on 06/05/2022.
 */

class SuperMajoritySpec extends AnyFlatSpec {

  val expectedWinners: List[PreferentialCandidate] = List.empty

  "Majority Algorithm Test" should
    "verify result" in
    assert(majorityAlgorithmVerification("01-candidates.json", "03-ballots.json") === expectedWinners)


  def majorityAlgorithmVerification(candidatesFile: String, ballotFile: String): List[PreferentialCandidate] =
    val candidates: List[PreferentialCandidate] = Parser.parseCandidates("src/main/resources/" + candidatesFile)
    val ballots: List[PreferentialBallot[PreferentialCandidate]] = Parser.parseBallot("src/main/resources/" + ballotFile)

    SuperMajority.run(ballots, candidates, 1, Rational(60,100)).map(_.candidate)
}
