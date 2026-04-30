local group = game.ui.group("test_group")

client.debug(true)
client.send("a")

group.text("my_new_text")
  .text("hi there")
  .color(color.rgb(100, 255, 255))
  .anchor("top_center")
  .shadow(true)
  .pos(vec2.of(0, 10))