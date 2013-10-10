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

package ch.unige.cui.smv.stratagem.sigmadd

import scala.collection.mutable.HashMap

/**
 * This class should be used as a mixin.
 */
trait SigmaDDRewritingCache extends SigmaDDRewriter {
  /**
   * A map where we keep our cache.
   */
  val operationCache = new HashMap[(Class[_ <: SigmaDDRewriter], SigmaDDImplType), Option[SigmaDDImplType]]

  /**
   * This function adds cache functionality to the SigmaDD rewriter.
   */
  abstract override def apply(sigmaDD: SigmaDDImplType): Option[SigmaDDImplType] = {
    operationCache.getOrElseUpdate((this.getClass(), sigmaDD), super.apply(sigmaDD))
  }

}