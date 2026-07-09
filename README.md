# Exam Queue Server

A backend service built for a **Distributed Systems** module during an Erasmus exchange. The project implements a virtual waiting-room system for exams or office hours: students join a queue with their question, and professors/supervisors can pull students out of the queue to help them — remotely, without a physical line.

Developed with a partner as part of a two-person team: this repository contains the **server**, implemented from scratch based only on functional requirements given by the professor. All architecture and protocol decisions were made independently.

## What it does

- Students join a shared queue and receive a live position/ticket
- Supervisors (professors/TAs) can mark themselves as available, get automatically assigned the next student in line, or pick a specific student directly
- Queue state is broadcast to all connected clients in real time, so everyone's view stays in sync
- Clients send periodic heartbeats; if a client disappears without disconnecting cleanly, it's automatically dropped from the queue after a timeout

## My contribution

- Full server implementation (this repo), in Java using JeroMQ (ZMQ bindings for the JVM)
- Queue and matching logic (student ↔ supervisor assignment)
- Heartbeat/timeout mechanism to detect dropped clients
- Joint design of the JSON message protocol between client and server (client implemented separately, in Flutter, by my project partner)

## Architecture

The server uses two ZeroMQ socket types for two different communication needs:

- **REQ/REP** (`tcp://*:5556`) — synchronous request/reply channel for actions that need a direct response: joining the queue, heartbeats, supervisor status changes
- **PUB/SUB** (`tcp://*:5555`) — one-way broadcast channel the server uses to push queue-state updates to all connected clients whenever the queue changes

Two independent `Queue` instances are maintained internally — one for students, one for supervisors — sharing the same underlying logic. Each client is tracked as a `QueueItem` with its own heartbeat timer(s), since one logical user can have multiple active connections (e.g. a page refresh).

Messages are serialized as JSON (via Gson). Message types are distinguished by which optional fields are present (e.g. presence of `status` vs. an outgoing chat-style message) rather than an explicit type tag.

## Tech stack

- Java 8+, Gradle
- [JeroMQ](https://github.com/zeromq/jeromq) — pure-Java ZeroMQ implementation
- [Gson](https://github.com/google/gson) — JSON (de)serialization

## Running it

```bash
./gradlew build
./gradlew run
```

The server listens on:
- `tcp://*:5556` — REQ/REP for client and supervisor requests
- `tcp://*:5555` — PUB/SUB for queue-state broadcasts

A compatible client is required to connect (the Flutter client from this project is maintained separately by my project partner).

## Known limitations

This was built under an academic deadline and hasn't been hardened for production use. Notably:

- Queue state (`ArrayList`/`HashMap`) is shared between the main server loop and per-client heartbeat timer threads without synchronization — under concurrent load this could cause race conditions (e.g. `ConcurrentModificationException` or lost updates). A single-writer event queue would be the cleaner fix.
- Some methods assume non-empty collections (e.g. accepting a client from an empty queue) without explicit guards.
- Exception handling is coarse in a few places (broad `catch (Exception e)`) rather than targeted.

These are documented here deliberately rather than hidden — happy to discuss the trade-offs and how I'd address them given more time.

---
*This README was drafted with the help of Claude (Anthropic), based on a review of the codebase and my own descriptions of the project.*
