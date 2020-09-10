# Pandemic Combat Aid System

## Descrição do problema

Quando o mundo é atingido por uma pandemia sem remédio imediato, além das habilidades dos profissionais de saúde, é preciso ter um bom sistema de informações para ajudar nas tomadas de decisões, visando amenizar ao máximo seus impactos.

Assim, ainda que não seja da área de saúde, você pode ajudar no combate. Para isso,  foi designado para desenvolver um sistema que irá coletar informações de todo país, organizá-las e prover informações com base nelas.

## Requisitos (MVP 1)

Você irá desenvolver uma **API RESTFul** (a ideia é que facilmente outros sistemas consigam se integrar para prover e obter dados), ao qual irá armazenar informação sobre os hospitais, seus recursos (pessoais e materiais), pacientes em atendimento (versões futuras), etc, ajudando no intercâmbio de recursos.

* **Adicionar hospitais**

  Um hospotal deve ter um *nome*, *endereço*, *cnpj*, *localização* (latitude, longitude, etc.).

  Ao adicionar o hospital, junto deve ser adicionado seus recursos atuais bem como seu percentual de ocupação.

* **Atualizar percentual de ocupação de um hospital**

  Um hospital deve poder reportar seu percentual de ocupação a todo instante, de forma que isso possa ser usado no processo de intercâmbio de recursos.

* **Hospitais não podem Adicionar/Remover recursos**

  Os recursos dos hospitais só podem ser alterados via intercâmbio. Aquisição de recursos avulso será feita em outra API, pois requer um processo específico.

* **Intercâmbio de recursos**

  Os hospitais poderão trocar recursos entre eles.

  Para isso, eles devem respeitar a tabela de valores abaixo, onde o valor do recurso é descrito em termos de pontos.

  Ambos os hospitais deverão oferecer a mesma quantidade de pontos. Por exemplo, 2 respiradores e 1 enfermeiro (2 x 5 + 1 x 3 = 13), valem o mesmo que 1 médico e 1 ambulância (1 x 3 + 1 x 10 = 13).
  Esta regra poderá ser quebrada caso algum hospital esteja com ocupação maior que 90%, onde ele poderá oferecer menos recursos que outro hospital no intercâmbio.

  Além de armazenar a negociação por questões de histórico, os itens deverão ser transferidos de um hospital a outro.

  | Item         | Pontos    |
  |------------- |-----------|
  | 1 Médico     |  3 pontos |
  | 1 Enfermeiro |  3 pontos |
  | 1 Respirador |  5 pontos |
  | 1 Tomógrafo  | 12 ponto  |
  | 1 Ambulância | 10 ponto  |

* **Relatórios**

  A API deve oferecer os seguintes relatórios:

  1. Porcentagem de hospitais com ocupação maior que 90%.
  2. Porcentagem de hospitais com ocupação menor que 90%.
  3. Quantidade média de cada tipo de recurso por hospital (Ex: 2 tomógrafos por hospital).
  4. Hospital em super-lotação (ocupação maior que 90%) a mais tempo.
  5. Hospital em abaixo de super-lotação (ocupação maior que 90%) a mais tempo.
  6. Histórico de negociação.

# Notas

1. Deverá ser utilizado Java, Spring boot, Spring Data, Hibernate (pode ser usado o banco de dados H2) e como gerenciador de dependência Maven ou Gradle.
2. Não será necessário autenticação.
3. Nós ainda nos preocupamos com uma programação adequada (código limpo) e técnicas de arquitetura, você deve demonstrar isso mesmo em meio a uma pandemia.
4. Não esqueça de documentar a sua API.
5. Sua API deve estar minimamente coberta por testes (Unitários e/ou integração).
6. Da descrição acima você pode escrever uma solução básica ou adicionar requisitos não descritos. Use seu tempo com sabedoria; Uma solução ótima e definitiva pode levar muito tempo para ser efetiva, então você deve trazer a melhor solução possível, que leve o mínimo de tempo, mas que ainda seja capaz de demonstrar suas habilidades.
7. Comente qualquer dúvida e cada decisão tomada.
