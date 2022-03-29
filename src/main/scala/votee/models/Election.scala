package votee.models

/**
 * Created by Abanda Ludovic on 29/03/2022.
 */

trait Election[C <: Candidate, B <: Ballot[C], W <: Winner[C]]:
  def run(ballots: List[B], candidates: List[C], vacancies: Int): List[W]

