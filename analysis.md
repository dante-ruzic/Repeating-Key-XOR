### Proof



* Let $c_x$ and $c_x^1$ represent the encrypted bytes of messages $m_x$ and $m_x^1$ using the key $k_{x \bmod \ell}$.
$$c_x = m_x \oplus k_{x \bmod \ell}$$
$$c_x^1 = m_x^1 \oplus k_{x \bmod \ell}$$
* By using exclusive or on the two ciphertexts together you get
$$c_x \oplus c_x^1 = (m_x \oplus k_{x \bmod \ell}) \oplus (m_x^1 \oplus k_{x \bmod \ell})$$
* Because the exclusive or operation is commutative and associative and any value using exclusive or with itself is 0 making it cancel out. 
$$c_x \oplus c_x^1 = m_x \oplus m_x^1 \oplus (k_{x \bmod \ell} \oplus k_{x \bmod \ell})$$
$$c_x \oplus c_x^1 = m_x \oplus m_x^1 \oplus 0 = m_x \oplus m_x^1$$
* While the cancelation of the exclusive or removes the protection, the plaintext isn't instantly revealed.
---

### Optional Bonus:

The cipher is completely vulnerable to manipulation because it does not provide ciphertext integrity.

* If an attacker knows the original plaintext byte $m_x$, they can force the decryption to yield a chosen byte $\widehat{m}_x$ by crafting a manipulated ciphertext byte $c_x^1$.
$$c_x^1 = c_x \oplus m_x \oplus \widehat{m}_x$$
* When the receiver attempts to decrypt $c_x^1$, the exclusive or properties allow the attacker's chosen byte to come out intact.

 Decrypted Byte $$ = c_x^1 \oplus k_{x \bmod \ell}$$

 Decrypted Byte $$ = (c_x \oplus m_x \oplus \widehat{m}_x) \oplus k_{x \bmod \ell}$$
* Substitute $c_x = m_x \oplus k_{x \bmod \ell}$.

 Decrypted Byte $$ = (m_x \oplus k_{x \bmod \ell}) \oplus m_x \oplus \widehat{m}_x \oplus k_{x \bmod \ell} = \widehat{m}_x$$
* The attacker must know or guess the original plaintext byte $m_x$.
* The attack succeeds because the exclusive or cipher is purely linear and bitwise. Modification of a specific bit in the cipher directly interacts with the same bit in the decrypted plaintext without needing to know the key.
