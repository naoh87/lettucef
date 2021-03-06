// Code generated by codegen/run; DO NOT EDIT
package dev.naoh.lettucef.api.commands

import io.lettuce.core.FlushMode


trait ScriptingCommandsF[F[_], K, V] {

  def scriptExists(digests: String*): F[Seq[Boolean]]
  
  def scriptFlush(): F[String]
  
  def scriptFlush(flushMode: FlushMode): F[String]
  
  def scriptKill(): F[String]
  
  def scriptLoad(script: String): F[String]
  
  def scriptLoad(script: Array[Byte]): F[String]
  
}
