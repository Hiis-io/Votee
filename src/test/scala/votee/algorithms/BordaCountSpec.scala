package votee.algorithms

import org.scalatest.flatspec.AnyFlatSpec
import votee.models.{PreferentialBallot, PreferentialCandidate}
import votee.utils.Parser

/**
 * Created by Abanda Ludovic on 07/05/2022.
 */

class BordaCountSpec extends AnyFlatSpec {

  val expectedWinners = List(PreferentialCandidate("a", "A"))

  "Borda Count Algorithm Test" should
    "verify result" in
    assert(algorithmVerification("01-candidates.json", "03-ballots.json") === expectedWinners)


  def algorithmVerification(candidatesFile: String, ballotFile: String): List[PreferentialCandidate] =
    val candidates: List[PreferentialCandidate] = Parser.parseCandidates("src/main/resources/" + candidatesFile)
    val ballots: List[PreferentialBallot[PreferentialCandidate]] = Parser.parseBallot("src/main/resources/" + ballotFile)

    BordaCount.run(ballots, candidates, 1).map(_.candidate)
}
