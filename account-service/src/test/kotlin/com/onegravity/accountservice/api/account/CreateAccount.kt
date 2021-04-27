package com.onegravity.accountservice.api.account

import com.onegravity.accountservice.persistence.model.account.AccountStatus
import com.onegravity.accountservice.util.testApplication
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.ktor.http.*

@Suppress("unused")
class CreateAccount : BehaviorSpec( {
    testApplication { testEngine ->
        `when`("I call POST /api/v1/admin/accounts") {
            val (account, status) = createAccount(testEngine, AccountStatus.Active)

            then("the response mustn't be null") {
                account shouldNotBe null
            }

            then("the response status should be OK") {
                status shouldBe HttpStatusCode.OK
            }

            then("the response body should be the created account") {
                verifyAccount(account!!)
                account.status shouldBe AccountStatus.Active
                account.createdAt shouldBe account.modifiedAt
            }
        }

        `when`("I call POST /api/v1/admin/accounts to create a deleted account") {
            val (account, status) = createAccount(testEngine, AccountStatus.Deleted)

            then("the response mustn't be null") {
                account shouldNotBe null
            }

            then("the response status should be OK") {
                status shouldBe HttpStatusCode.OK
            }

            then("the returned account object should have status deleted") {
                account!!.status shouldBe AccountStatus.Deleted
                account.createdAt shouldBe account.modifiedAt
            }
        }
    }
} )
