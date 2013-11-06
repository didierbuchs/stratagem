/*
Stratagem is a model checker for transition systems described using rewriting
rules and strategies.
Copyright (C) 2013 - SMV@Geneva University.
Program written by Edmundo Lopez Bobeda <edmundo [at] lopezbobeda.net>.
This program is free software; you can redistribute it and/or modify
it under the  terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.
This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.
You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package ch.unige.cui.smv.stratagem.ts

/**
 * Represents the One strategy.
 * @param S the strategy it takes as parameter.
 * @param n represents which subterm will be rewritten by this one.
 * @author mundacho
 *
 */
case class One(S: Strategy, n: Int) extends NonVariableStrategy {
  require(n >= 0)
}

/**
 * Companion object. It adds a default constructor.
 */
object One {
  def apply(S: Strategy) = new One(S, 0)
}