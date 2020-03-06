public static void persistData(WizardDao dao){
  Spell spell=new Spell("Fireball");
  Spellbook spellbook=new Spellbook("Book of fire");
  spellbook.getSpells().add(spell);
  Wizard wizard=new Wizard("Jugga");
  wizard.getSpellbooks().add(spellbook);
  dao.persist(wizard);
}
