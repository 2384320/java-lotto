package lotto.Domain;

import java.util.List;

public class WinningLotto {
    private final LottoNumber bonusNumber;
    private final Lotto winningLotto;

    public WinningLotto(LottoNumber bonusNumber, Lotto winningLotto) {
        validateDuplicate(bonusNumber, winningLotto);
        addBonusNumberInWinningLotto(bonusNumber, winningLotto);

        this.bonusNumber = bonusNumber;
        this.winningLotto = winningLotto;
    }

    private void validateDuplicate(LottoNumber bonusNumber, Lotto winningLotto) {
        if (winningLotto.getNumbers().contains(bonusNumber.mapToInt()))
            throw new IllegalArgumentException("[ERROR] 번호는 중복 숫자가 없어야 합니다.");
    }

    private void addBonusNumberInWinningLotto(LottoNumber bonusNumber, Lotto winningLotto) {
        winningLotto.getNumbers().add(bonusNumber.mapToInt());
    }

    public int getBonusNumber() {
        return bonusNumber.mapToInt();
    }

    public List<Integer> getWinningLotto() {
        return winningLotto.getNumbers();
    }
}
