package votee.algorithms

import org.scalatest.flatspec.AnyFlatSpec
import votee.models.{PreferentialBallot, PreferentialCandidate}
import votee.utils.Parser
import spire.math.Rational

/**
 * Created by Abanda Ludovic on 06/05/2022.
 */

class SuperMajoritySpec extends AnyFlatSpec {

  val expectedWinners = List.empty

  "Super-Majority Algorithm Test" should
    "verify result" in
    assert(algorithmVerification("01-candidates.json", "03-ballots.json") === expectedWinners)

  def algorithmVerification(candidatesFile: String, ballotFile: String): List[PreferentialCandidate] =
    val candidates: List[PreferentialCandidate] = Parser.parseCandidates("src/main/resources/" + candidatesFile)
    val ballots: List[PreferentialBallot[PreferentialCandidate]] = Parser.parseBallot("src/main/resources/" + ballotFile)

    SuperMajority.run(ballots, candidates, 1, Rational(6,10)).map(_.candidate)
}