package io.hiis.votee.algorithms

import io.hiis.votee.UnitSpec
import io.hiis.votee.algorithms.ExhaustiveBallot
import io.hiis.votee.models.PreferentialCandidate
import io.hiis.votee.utils.Parser
import org.scalatest.flatspec.AnyFlatSpec

/** Created by Abanda Ludovic on 09/05/2022. */

class ExhaustiveBallotSpec extends AnyFlatSpec with UnitSpec:

  val expectedWinners = List(PreferentialCandidate("b", "B"))

  "Exhaustive Ballot Algorithm Test" should
    "verify result" in
    assert(algorithmVerification("01-candidates.json", "01-ballots.json") === expectedWinners)

  def algorithmVerification(
      candidatesFile: String,
      ballotFile: String
  ): Seq[PreferentialCandidate] =
    val inputs = candidatesAndBallots(candidatesFile, ballotFile)
    ExhaustiveBallot.run(inputs._2, inputs._1, 1).map(_.candidate)
