package dev.naoh.lettucef.api

import dev.naoh.lettucef.streams.ManagedPubSubExtensionOps
import dev.naoh.lettucef.streams.StreamCommandApiOps

package object streams extends StreamCommandApiOps with ManagedPubSubExtensionOps