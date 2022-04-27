package votee.algorithms
import org.scalatest.flatspec.AnyFlatSpec
import votee.models.{PreferentialBallot, PreferentialCandidate}
import votee.utils.Parser

/**
 * Created by Abanda Ludovic on 27/04/2022.
 */

class MajoritySpec extends AnyFlatSpec {

  val expectedWinners = List(PreferentialCandidate("a", "A"))

  "Majority Algorithm Test" should
    "verify result" in
    assert(majorityAlgorithmVerification("01-candidates.json", "03-ballots.json") === expectedWinners)


  def majorityAlgorithmVerification(candidatesFile: String, ballotFile: String): List[PreferentialCandidate] =
    val candidates: List[PreferentialCandidate] = Parser.parseCandidates("src/main/resources/" + candidatesFile)
    val ballots: List[PreferentialBallot[PreferentialCandidate]] = Parser.parseBallot("src/main/resources/" + ballotFile)

    Majority.run(ballots, candidates, 1).map(_.candidate)
}
