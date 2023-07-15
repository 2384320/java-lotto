package lotto.UI;

import lotto.Domain.LotteryTickets;
import lotto.Domain.Lotto;
import lotto.Domain.LottoNumber;
import lotto.Domain.Winning;

import java.util.List;
import java.util.stream.Collectors;

public class Output {
    private static final String PURCHASE_LOTTERY = "개를 구매했습니다.";
    private static final String RESULT_VIEW = "당첨 통계\n---";
    private static final String RESULT_BONUS_CORRECT = ", 보너스 볼 일치 ";
    private static final String RESULT_BONUS_NOT_CORRECT = " ";

    public static void printExceptionMessage(String exceptionMessage) {
        System.out.println(exceptionMessage);
    }

    public static void printPurchaseLotteryCount(int lotteryCount) {
        System.out.println(lotteryCount + PURCHASE_LOTTERY);
    }

    public static void printLotteryTickets(LotteryTickets lotteryTickets) {
        for (Lotto lotto : lotteryTickets.getLotteryTickets()) {
            System.out.println(convertLottoNumberToInt(lotto.getNumbers()));
        }
    }

    public static List<Integer> convertLottoNumberToInt(List<LottoNumber> lotteryNumbers) {
        return lotteryNumbers.stream()
                .map(LottoNumber::mapToInt)
                .collect(Collectors.toList());
    }

    public static void printResultView() {
        System.out.println(RESULT_VIEW);
    }

    public static void printWinningScore(int correctCount, String winningPrize, int winningScore, Winning winning) {
        System.out.println(getCorrectCount(correctCount, winning) + "개 일치"
                + getBonusCorrectString(winning)
                + "(" + winningPrize + "원) - "
                + winningScore + "개"
        );
    }

    public static int getCorrectCount(int correctCount, Winning winning) {
        if (winning == Winning.SECOND) return correctCount - 1;
        return correctCount;
    }

    public static String getBonusCorrectString(Winning winning) {
        if (winning == Winning.SECOND) return RESULT_BONUS_CORRECT;
        return RESULT_BONUS_NOT_CORRECT;
    }

    public static void printRateOfReturn(String rateOfReturn) {
        System.out.println("총 수익률은 " + rateOfReturn + "%입니다.");
    }

    public static void outputPurchasePrice() {
        System.out.println("구입금액을 입력해 주세요.");
    }

    public static void outputWinningNumbers() {
        System.out.println("당첨 번호를 입력해주세요.");
    }

    public static void outputBonusNumber() {
        System.out.println("보너스 번호를 입력해 주세요.");
    }
}
