package resolver

import graphql.schema.DataFetcher
import models.*
import queries.Queries
import java.time.Instant

class QueryResolver(queries: Queries) {

    val mortalityRate: DataFetcher<Double> = DataFetcher { env ->
        val context = env.getContext<Context>()
        val age = env.getArgument<Int>("age")
        val year = env.getArgument<Int>("year")
        queries.getMortalityRate(context.account, age, year)
    }

    val interestRate: DataFetcher<Double> = DataFetcher { env ->
        val context = env.getContext<Context>()
        val age = env.getArgument<Int>("age")
        queries.getInterest(context.account, age)
    }

    val assumptions: DataFetcher<Assumptions> = DataFetcher { env ->
        val context = env.getContext<Context>()
        queries.getAssumptions(context.account)
    }
    val participant: DataFetcher<FinalParticipant> = DataFetcher { env ->
        val context = env.getContext<Context>()
        val timestampAsText = env.arguments["timestamp"] as String?
        val timestamp = if (timestampAsText == null) null else Instant.parse(timestampAsText)
        val id = env.getArgument<String>("id")
        queries.getParticipant(context.account, id, timestamp)
    }

    val participants: DataFetcher<List<FinalParticipant>> = DataFetcher { env ->
        val context = env.getContext<Context>()
        queries.getParticipants(context.account)
    }

    val accessCode: DataFetcher<String?> = DataFetcher { env ->
        val id = env.getArgument<String>("username")
        val name = env.getArgument<String>("password")

        queries.getAccessCodeForAccount(id, name)
    }

    // helper to query events in the system
    val events: DataFetcher<List<TransportEvent>> = DataFetcher {
        queries.getEvents()
    }
}