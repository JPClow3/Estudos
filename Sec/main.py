from Crypto.Cipher import AES
from Crypto.Random import get_random_bytes
from Crypto.Util.Padding import pad, unpad

def cifrar_mensagem():
    chave = get_random_bytes(16)
    iv = get_random_bytes(16)
    texto = input("Digite o texto a ser criptografado: ").encode()
    texto_com_padding = pad(texto, 16)
    cifra = AES.new(chave, AES.MODE_CBC, iv)
    texto_cifrado = cifra.encrypt(texto_com_padding)
    with open('dados_cripto.bin', 'wb') as f:
        f.write(chave + iv + texto_cifrado)
    print("Criptografia concluída.")
    print("Chave:", chave.hex())
    print("IV:", iv.hex())
    print("Texto cifrado:", texto_cifrado.hex())

def decifrar_mensagem():
    with open('dados_cripto.bin', 'rb') as f:
        dados = f.read()
    chave = dados[:16]
    iv = dados[16:32]
    texto_cifrado = dados[32:]
    cifra = AES.new(chave, AES.MODE_CBC, iv)
    texto_com_padding = cifra.decrypt(texto_cifrado)
    texto_original = unpad(texto_com_padding, 16)
    print("Texto original:", texto_original.decode())

cifrar_mensagem()
decifrar_mensagem()