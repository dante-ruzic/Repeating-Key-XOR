### Proof
#### 1
To prove that decryption reverses encryption for message $m \in \mathbb{B}^*$ and key $k \in \mathbb{B}^+$, it relies on the self-inverse identity of the exclusive or operation.

* The encryption of a single byte at index $x$ is defined as $c_x = m_x \oplus k_{x \bmod \ell}$.
* Decryption applies the same key byte to the ciphertext.

$$c_x \oplus k_{x \bmod \ell} = (m_x \oplus k_{x \bmod \ell}) \oplus k_{x \bmod \ell}$$

* Because any exclusive or value with itself is zero, and a value of exclusive or with zero remains unchanged, the key cancels out.

$$(m_x \oplus k_{x \bmod \ell}) \oplus k_{x \bmod \ell} = m_x \oplus 0 = m_x$$

* If the message is empty (a length of $0$), the encryption/decryption loops run zero times, meaning the empty string returns an empty string.
#### 2
If an attacker knows a plaintext byte $m_x$ and its corresponding ciphertext byte $c_x$, they can easily find the key byte.

* By rearranging the encryption equation, the attacker learns the key byte used at that specific position.

$$k_{x \bmod \ell} = c_x \oplus m_x$$

* Because the key repeats every $\ell$ bytes, extracting this single key byte compromises every other message position encrypted with it. Every index $j$ where $j \equiv x \pmod{\ell}$.
#### 3
* Let $c_x$ and $c_x^1$ represent the encrypted bytes of messages $m_x$ and $m_x^1$ using the key $k_{x \bmod \ell}$.
$$c_x = m_x \oplus k_{x \bmod \ell}$$
$$c_x^1 = m_x^1 \oplus k_{x \bmod \ell}$$
* By using exclusive or on the two ciphertexts together you get
$$c_x \oplus c_x^1 = (m_x \oplus k_{x \bmod \ell}) \oplus (m_x^1 \oplus k_{x \bmod \ell})$$
* Because the exclusive or operation is commutative and associative and any value using exclusive or with itself is 0 making it cancel out. 
$$c_x \oplus c_x^1 = m_x \oplus m_x^1 \oplus (k_{x \bmod \ell} \oplus k_{x \bmod \ell})$$
$$c_x \oplus c_x^1 = m_x \oplus m_x^1 \oplus 0 = m_x \oplus m_x^1$$
* While the cancelation of the exclusive or removes the protection, the plaintext isn't instantly revealed.
#### 4
A true one-time pad provides secrecy but requires strict conditions.

* **OTP Conditions:** The key must be completely uniform (perfectly random), at least as long as the message itself, distributed securely, and used only a single time.
* **Repeating-key XOR Violations:** This cipher violates the length condition whenever the key is shorter than the message ($\ell < n$). 
* It violates the single-use condition whenever the key repeats within a message or when the same key is reused across multiple separate messages.
#### 5
Even under the best conditions, this cipher has significant limitations.

* **Case (a) Short repeating key:** Confidentiality is compromised. Because the key repeats an attacker can use plaintexts or ciphertext cancellation to expose the message.
* **Case (b) Uniform key $\ge$ message length (never reused):** This scenario provides confidentiality, acting effectively like a one-time pad. 
* **Lack of Integrity:** However, even Case (b) does not provide ciphertext integrity or authenticity. As shown in the malleability bonus an attacker can flip bits in the ciphertext to alter the decrypted plaintext without knowing the key. The system remains dependent on secure key distribution to work
---

### Optional Bonus:

The cipher is completely vulnerable to manipulation because it does not provide ciphertext integrity.

* If an attacker knows the original plaintext byte $m_x$, they can force the decryption to yield a chosen byte $\widehat{m}_x$ by crafting a manipulated ciphertext byte $c_x^1$.
$$c_x^1 = c_x \oplus m_x \oplus \widehat{m}_x$$
* When the receiver attempts to decrypt $c_x^1$, the exclusive or properties allow the attacker's chosen byte to come out intact.

 Decrypted Byte $= c_x^1 \oplus k_{x \bmod \ell}$

 Decrypted Byte $$= (c_x \oplus m_x \oplus \text{(widehat) modified byte: }m_x) \oplus k_{x \bmod \ell}$$
* Substitute $c_x = m_x \oplus k_{x \bmod \ell}$.

 Decrypted Byte $$= (m_x \oplus k_{x \bmod \ell}) \oplus m_x \oplus \text{(widehat) modified byte: }m_x \oplus k_{x \bmod \ell} = \widehat{m}_x$$
* The attacker must know or guess the original plaintext byte $m_x$.
* The attack succeeds because the exclusive or cipher is purely linear and bitwise. Modification of a specific bit in the cipher directly interacts with the same bit in the decrypted plaintext without needing to know the key.
