/*
 * This software is in the public domain under CC0 1.0 Universal plus a 
 * Grant of Patent License.
 * 
 * To the extent possible under law, the author(s) have dedicated all
 * copyright and related and neighboring rights to this software to the
 * public domain worldwide. This software is distributed without any
 * warranty.
 * 
 * You should have received a copy of the CC0 Public Domain Dedication
 * along with this software (see the LICENSE.md file). If not, see
 * <http://creativecommons.org/publicdomain/zero/1.0/>.
 */

import org.moqui.Moqui
import org.moqui.context.ExecutionContext
import org.moqui.entity.EntityList
import org.moqui.entity.EntityValue
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Shared
import spock.lang.Specification

import java.sql.Timestamp

/* To run these make sure moqui, and mantle are in place and run:
    "gradle cleanAll load runtime/mantle/mantle-usl:test"
   Or to quick run with saved DB copy use "gradle loadSave" once then each time "gradle reloadSave runtime/mantle/mantle-usl:test"
 */
class TicketFlow extends Specification {
    @Shared
    protected final static Logger logger = LoggerFactory.getLogger(TicketFlow.class)
    @Shared
    ExecutionContext ec
    @Shared
    String cartOrderId = null, cartOrderPartSeqId
    @Shared
    String inventoryOrderId = null
    @Shared
    Map setInfoOut, shipResult
    @Shared
    String b2bPaymentId, b2bShipmentId, b2bCredMemoId
    @Shared
    long effectiveTime = System.currentTimeMillis()
    // no longer needed: @Shared boolean kieEnabled = false
    @Shared
    long totalFieldsChecked = 0

    def setupSpec() {
        // init the framework, get the ec
        ec = Moqui.getExecutionContext()
        // set an effective date so data check works, etc
        ec.user.setEffectiveTime(new Timestamp(effectiveTime))
        // no longer needed: kieEnabled = ec.factory.getToolFactory("KIE") != null

        ec.entity.tempSetSequencedIdPrimary("mantle.work.effort.WorkEffort", 55500, 10)
        ec.entity.tempSetSequencedIdPrimary("mantle.work.effort.WorkEffortNote", 55500, 50)
        ec.entity.tempSetSequencedIdPrimary("mantle.work.effort.WorkEffortServiceLevelAgreement", 55500, 10)
    }

    def cleanupSpec() {
        ec.entity.tempResetSequencedIdPrimary("mantle.work.effort.WorkEffort")
        ec.entity.tempResetSequencedIdPrimary("mantle.work.effort.WorkEffortNote")
        ec.entity.tempResetSequencedIdPrimary("mantle.work.effort.WorkEffortServiceLevelAgreement")

        ec.destroy()

        ec.factory.waitWorkerPoolEmpty(50) // up to 5 seconds
    }

    def setup() {
        ec.artifactExecution.disableAuthz()
    }

    def cleanup() {
        ec.artifactExecution.enableAuthz()
    }

    def "简单测试"() {
        when:
        ec.user.loginUser("newmann.hu", "moqui")
        logger.info("登录结束")
        then:
        0==0
    }


}